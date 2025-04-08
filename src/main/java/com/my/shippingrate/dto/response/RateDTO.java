package com.my.shippingrate.dto.response;

import java.math.BigDecimal;

public record RateDTO(String courier, BigDecimal rate) {
}
