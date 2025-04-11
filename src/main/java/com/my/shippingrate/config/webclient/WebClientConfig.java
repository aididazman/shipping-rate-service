package com.my.shippingrate.config.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.*;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${jnt.base-url}")
    private String jntBaseUrl;

    @Value("${citylink.base-url}")
    private String citylinkBaseUrl;

    @Bean
    @Qualifier("jntWebClient")
    public WebClient jntWebClient(WebClient.Builder builder) {

        return builder
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
                .baseUrl(jntBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/122.0.0.0 Safari/537.36")
                .defaultHeader(HttpHeaders.ORIGIN, "https://www.jtexpress.my")
                .defaultHeader(HttpHeaders.REFERER, "https://www.jtexpress.my/shipping-rates")
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
                .build();
    }

}
