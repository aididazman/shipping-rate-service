package com.my.shippingrate.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum ProviderType {
    CITYLINK("citylink");

    private final String courier;
}
