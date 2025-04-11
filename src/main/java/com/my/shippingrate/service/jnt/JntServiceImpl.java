package com.my.shippingrate.service.jnt;

import com.my.shippingrate.dto.request.jnt.JntRequestDTO;
import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.RateDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.service.ShippingRateService;
import com.my.shippingrate.service.redis.RedisService;
import com.my.shippingrate.utils.CommonUtil;
import com.my.shippingrate.utils.ProviderType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.jsoup.Jsoup;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JntServiceImpl implements ShippingRateService {

    private final WebClient webClient;
    private final RedisService redisService;

    @Value("${jnt.api.shipping-rate.url}")
    private String jntUrl;

    @Value("${jnt.dryrun}")
    private boolean dryRun;

    public JntServiceImpl(@Qualifier("jntWebClient") WebClient webClient, RedisService redisService) {
        this.webClient = webClient;
        this.redisService = redisService;
    }

    @SneakyThrows
    @Override
    public ResponseWrapperDTO calculateShippingRate(PayloadDTO payloadDTO, List<RateDTO> rateDTOList) {

        JntRequestDTO request = (JntRequestDTO) payloadDTO;

        String cacheKey = CommonUtil.generateCacheKey(request);
        log.info("Generated cache key : {}", cacheKey);
        ResponseWrapperDTO cachedResponse = redisService.getValueByCacheKey(cacheKey);

        if (cachedResponse != null) {
            log.info("Fetch shipping rate from cache");
            return cachedResponse;
        }

        // Get the _token and cookies
        Map<String, String> data = getCookiesAndToken();
        String cookies = data.get("cookies");
        String token = data.get("_token");

        // Construct form data from payload
        MultiValueMap<String, String> formData = constructRequest(token, request);

        // Make the initial POST request to get the response
        String htmlResponse = webClient.post()
                .uri("/shipping-rates")
                .header(HttpHeaders.COOKIE, cookies)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(HttpStatusCode::is3xxRedirection, response -> {
                    // Log the redirect status code and location header
                    log.info("Redirect Status Code: {}", response.statusCode());
                    String locationHeader = response.headers().header(HttpHeaders.LOCATION).get(0);
                    log.info("Redirect location: {}", locationHeader);
                    return Mono.empty();
                })
                .toEntity(String.class)
                .map(HttpEntity::getBody)
                .block();

        String rate = extractDataFromResponse(htmlResponse);
        return fetchRate(rateDTOList, cacheKey, rate);
    }

    private MultiValueMap<String, String> constructRequest(String token, JntRequestDTO jntRequestDTO) {
        log.info("Construct request with payloadDTO: {}", jntRequestDTO);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("_token", token);
        formData.add("shipping_rates_type", jntRequestDTO.getShippingRatesType());
        formData.add("sender_postcode", jntRequestDTO.getSenderPostcode());
        formData.add("receiver_postcode", jntRequestDTO.getReceiverPostcode());
        formData.add("weight", String.valueOf(jntRequestDTO.getWeight()));

        String length = jntRequestDTO.getLength() != null ? String.valueOf(jntRequestDTO.getLength()) : "";
        formData.add("length", length);

        String width = jntRequestDTO.getWidth() != null ? String.valueOf(jntRequestDTO.getWidth()) : "";
        formData.add("width", width);

        String height = jntRequestDTO.getHeight() != null ? String.valueOf(jntRequestDTO.getHeight()) : "";
        formData.add("height", height);

        formData.add("destination_country", jntRequestDTO.getDestinationCountry());
        formData.add("shipping_type", jntRequestDTO.getShippingType());
        formData.add("insurance", Optional.ofNullable(jntRequestDTO.getInsurance()).orElse(""));
        formData.add("item_value", String.valueOf(jntRequestDTO.getItemValue()));

        return formData;
    }

    private Map<String,String> getCookiesAndToken() {

        log.info("Get cookies and _token parameter from J&T HTML page");
        Map<String, String> data = new HashMap<>();

        String html = webClient.get()
                .uri("/shipping-rates")
                .exchangeToMono(response -> {
                    // Extract Set-Cookie headers
                    List<String> setCookies = response.headers().asHttpHeaders().get(HttpHeaders.SET_COOKIE);
                    // Join cookies for use in the next request
                    String cookieHeader = setCookies.stream()
                            .map(cookie -> cookie.split(";", 2)[0]) // only keep name=value
                            .collect(Collectors.joining("; "));
                    data.put("cookies", cookieHeader);

                    // return the response body (HTML content)
                    return response.bodyToMono(String.class);
                })
                .block();

        String token = fetchTokenFromJntPage(html);
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

    public String extractDataFromResponse(String htmlResponse) throws IOException {

        String content = htmlResponse;

        if (dryRun) {
            log.info("Dry-run is true. Running with sample HTML response");
            Resource resource = new ClassPathResource("JNT_SAMPLE_HTML_RESPONSE.html");
            content = new String(Files.readAllBytes(resource.getFile().toPath()));
        }

        Document doc = Jsoup.parse(content);
        // Find the first table and locate the "Total (incl. Tax)" row
        Element totalRow = doc.select("div.table-responsive.d-block.d-sm-none table tbody tr:has(th:contains(Total (incl. Tax)))").first();

        if (totalRow == null)
            throw new IllegalStateException("Incomplete response from J&T");

        // Extract the value of the second <td> in that row (which contains the "Total (incl. Tax)" value)
        return totalRow.select("td:nth-of-type(1)").text();
    }

    private ResponseWrapperDTO fetchRate(List<RateDTO> rateDTOList, String cacheKey, String rateFromHtml) {

        BigDecimal rate = new BigDecimal(rateFromHtml);
        RateDTO rateDTO =  new RateDTO(ProviderType.JNT.getCourier(), rate);
        rateDTOList.add(rateDTO);

        ResponseWrapperDTO responseWrapperDTO = new ResponseWrapperDTO(rateDTOList);
        log.info("Saving response to redis for caching purpose");
        redisService.saveCache(responseWrapperDTO, cacheKey);

        log.info("Complete calculate shipping rate with response: {}",responseWrapperDTO);
        return responseWrapperDTO;
    }

    @Override
    public String getProvider() {
        return ProviderType.JNT.name();
    }
}
