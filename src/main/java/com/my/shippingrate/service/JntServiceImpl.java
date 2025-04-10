package com.my.shippingrate.service;

import com.my.shippingrate.dto.request.jnt.JntRequestDTO;
import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.RateDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.utils.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.jsoup.Jsoup;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JntServiceImpl implements ShippingRateService {

    private final WebClient webClient;

    @Value("jnt.api.shipping-rate.url")
    private String jntUrl;

    public JntServiceImpl(@Qualifier("jntWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResponseWrapperDTO calculateShippingRate(PayloadDTO payloadDTO, List<RateDTO> rateDTOList) {

        // Step 1: Get the XSRF token and cookies
        Map<String, String> data = getXsrfTokenFromJntPage();
        String cookies = data.get("cookies");
        String xsrfToken = data.get("xsrfToken");

        // Step 2: Construct form data from payload
        MultiValueMap<String, String> formData = constructRequest(data, payloadDTO);

        // Step 3: Submit form via POST
        ClientResponse postResponse = webClient.post()
                .uri("/shipping-rates")
                .header(HttpHeaders.COOKIE, cookies)
                .header("X-XSRF-TOKEN", xsrfToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ORIGIN, "https://www.jtexpress.my")
                .header(HttpHeaders.REFERER, "https://www.jtexpress.my/shipping-rates")
                .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/123 Safari/537.36")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .block();

        HttpStatusCode status = postResponse.statusCode();
        log.info("POST response status: {}", status);

        if (status.is3xxRedirection()) {
            // Step 4: Manually follow redirect using same cookies
            String location = postResponse.headers().header("Location").stream().findFirst().orElse(null);
            log.info("Redirecting to: {}", location);

            if (location != null) {
                // Make GET request to the redirect URL
                String html = webClient.get()
                        .uri(location)
                        .header(HttpHeaders.COOKIE, cookies)
                        .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/123 Safari/537.36")
                        .header(HttpHeaders.REFERER, "https://www.jtexpress.my/shipping-rates")
                        .retrieve()
                        .bodyToMono(String.class)
                        .doOnNext(body -> log.info("Final HTML content:\n{}", body))
                        .block();

                // TODO: Extract shipping rate info from `html`
            }
        } else {
            // If not redirected, log full response
            String html = postResponse.bodyToMono(String.class)
                    .doOnNext(body -> log.warn("Unexpected response:\n{}", body))
                    .block();
        }

        return null; // Replace with final parsed result later
    }

    private MultiValueMap<String, String> constructRequest(Map<String, String> data, PayloadDTO payloadDTO) {
        log.info("Construct request with payloadDTO: {}", payloadDTO);

        JntRequestDTO jntRequestDTO = (JntRequestDTO) payloadDTO;
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("_token", data.get("_token"));
        formData.add("shipping_rates_type", jntRequestDTO.getShippingRatesType());
        formData.add("sender_postcode", jntRequestDTO.getSenderPostcode());
        formData.add("receiver_postcode", jntRequestDTO.getReceiverPostcode());
        formData.add("weight", String.valueOf(jntRequestDTO.getWeight()));
        formData.add("length", String.valueOf(jntRequestDTO.getLength()));
        formData.add("width", String.valueOf(jntRequestDTO.getWidth()));
        formData.add("height", String.valueOf(jntRequestDTO.getHeight()));
        formData.add("destination_country", jntRequestDTO.getDestinationCountry());
        formData.add("shipping_type", jntRequestDTO.getShippingType());
        formData.add("insurance", jntRequestDTO.getInsurance());
        formData.add("item_value", String.valueOf(jntRequestDTO.getItemValue()));

        return formData;
    }

    private Map<String,String> getXsrfTokenFromJntPage() {
        log.info("Get XSRF token and cookies from J&T HTML page");
        Map<String, String> data = new HashMap<>();

        String test = webClient.get()
                .uri("/shipping-rates")
                .exchangeToMono(response -> {
                    // Extract Set-Cookie headers
                    List<String> setCookies = response.headers().asHttpHeaders().get(HttpHeaders.SET_COOKIE);
                    // Join cookies for use in the next request
                    String cookieHeader = setCookies.stream()
                            .map(cookie -> cookie.split(";", 2)[0]) // only keep name=value
                            .collect(Collectors.joining("; "));
                    data.put("cookies", cookieHeader);

                    setCookies.forEach(cookie -> {
                        if (cookie.contains("XSRF-TOKEN")) {
                            String xsrfToken = cookie.split("=")[1].split(";")[0];
                            data.put("xsrfToken", xsrfToken);
                        }
                    });

                    setCookies.forEach(cookie -> {
                        if (cookie.contains("jt_express_malaysia_session")) {
                            String sessionToken = cookie.split("=")[1].split(";")[0];
                            data.put("sessionToken", sessionToken);
                        }
                    });

                    // Get the response body (HTML content)
                    return response.bodyToMono(String.class)
                            .doOnNext(html -> data.put("html", html));
                })
                .block();

        String htmlContent = data.get("html");
        String token = fetchTokenFromJntPage(htmlContent);
        data.put("_token", token);

        return data;
    }

    private String fetchTokenFromJntPage(String htmlContent) {
        log.info("Get _token from J&T HTML page");
        if (StringUtils.isEmpty(htmlContent)) {
            throw new IllegalArgumentException("HTML content cannot be null or empty");
        }

        // Parse HTML and extract _token
        Document document = Jsoup.parse(htmlContent);
        Element tokenElement = document.select("input[name=_token]").first();

        if (StringUtils.isEmpty(tokenElement.val())) {
            throw new IllegalArgumentException("Extracted token value is empty");
        }

        return tokenElement.val();
    }

    public List<Map<String, String>> extractDataFromResponse(String htmlResponse) {

        List<Map<String, String>> result = new ArrayList<>();

        Document doc = Jsoup.parse(htmlResponse);

        // Select only the first table inside the mobile-view div
        Element table = doc.selectFirst("div.d-block.d-sm-none table");

        if (table != null) {
            Elements rows = table.select("tr");

            // First row = headers (e.g. Goods Type, Parcel, Document)
            List<String> headers = new ArrayList<>();
            Elements headerCells = rows.get(0).select("th, td");

            for (int i = 1; i < headerCells.size(); i++) { // skip first header (label name)
                headers.add(headerCells.get(i).text().trim());
            }

            // Process rest of the rows
            for (int col = 0; col < headers.size(); col++) {
                Map<String, String> entry = new LinkedHashMap<>();
                entry.put("Type", headers.get(col)); // e.g. Parcel, Document

                for (int row = 1; row < rows.size(); row++) {
                    Elements cells = rows.get(row).select("th, td");
                    String label = cells.get(0).text().trim(); // e.g. Shipping Rates
                    String value = cells.get(col + 1).text().trim(); // get corresponding value

                    entry.put(label, value);
                }

                result.add(entry);
            }
        }

        return result;
    }

    private String extractRedirectUrl(String metaContent) {
        // e.g. content="0;url='https://www.jtexpress.my/some-page'"
        int urlStart = metaContent.indexOf("url='") + 5;
        int urlEnd = metaContent.indexOf("'", urlStart);
        return metaContent.substring(urlStart, urlEnd);
    }




    @Override
    public String getProvider() {
        return ProviderType.JNT.name();
    }
}
