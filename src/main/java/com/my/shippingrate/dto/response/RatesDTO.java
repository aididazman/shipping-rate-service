package com.my.shippingrate.dto.response;

import java.math.BigDecimal;

public record RatesDTO(String courier, BigDecimal rate) {
}
