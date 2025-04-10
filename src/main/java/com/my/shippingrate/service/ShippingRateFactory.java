package com.my.shippingrate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ShippingRateFactory {

    private final Map<String, ShippingRateService> serviceMap;

    @Autowired
    public ShippingRateFactory(List<ShippingRateService> services) {
        this.serviceMap = services.stream()
                .collect(Collectors.toMap(
                        service -> service.getProvider().toUpperCase(),
                        Function.identity()
                ));
    }

    public ShippingRateService getService(String provider) {

        ShippingRateService service = serviceMap.get(provider.toUpperCase());
        log.info("Fetch shipping rate for service: {}", service);

        if (service == null) {
            log.warn("Unsupported provider requested: {}", provider);
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        return service;
    }
}