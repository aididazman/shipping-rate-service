package com.my.shippingrate.config.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class WebClientLogging {

    public static WebClient.Builder addLoggingFilter(WebClient.Builder builder) {
        return builder.filter(logRequest());
    }

    private static ExchangeFilterFunction logRequest() {
        return (request, next) -> {
            log.info("Request: " + request.method() + " " + request.url());
            return next.exchange(request);
        };
    }
}
