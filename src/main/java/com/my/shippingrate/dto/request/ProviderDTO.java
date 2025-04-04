package com.my.shippingrate.dto.request;

import java.util.Map;

public record ProviderDTO(String provider, Map<String, Object> payload) {}
