package com.my.shippingrate.dto.response.jnt;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JntResponseDTO {
    private String goodsType;
    private BigDecimal shippingRates;
    private BigDecimal insuranceCharges;
    private BigDecimal totalInclTax;
}
