package com.my.shippingrate.config.webclient;

import com.my.shippingrate.utils.ProviderType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${jnt.base-url}")
    private String jntBaseUrl;

    @Value("${citylink.base-url}")
    private String citylinkBaseUrl;

    @Bean
    @Qualifier("jntWebClient")
    public WebClient jntWebClient(WebClient.Builder builder) {
        HttpClient httpClient = HttpClient.create().followRedirect(true);

        return builder
                .baseUrl(jntBaseUrl)
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/122.0.0.0 Safari/537.36")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .apply(WebClientLogging::addLoggingFilter)
                .build();
    }

    @Bean
    @Qualifier("cityLinkWebClient")
    public WebClient cityLinkWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(citylinkBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/122.0.0.0 Safari/537.36")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .apply(WebClientLogging::addLoggingFilter)
                .build();
    }

}
