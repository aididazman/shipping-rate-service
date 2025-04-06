package com.my.shippingrate.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum ProviderType {
    JNT("https://www.jtexpress.my/shipping-rates"),
    CITYLINK("https://www.citylinkexpress.com/calculator/");

    private final String baseUrl;
}
