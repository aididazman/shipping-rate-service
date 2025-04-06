package com.my.shippingrate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "J&T Shipping Rate Request")
public class JntRequestDTO extends PayloadDTO {
    private String _token;
    private String shipping_rates_type;
    private String sender_postcode;
    private String receiver_postcode;
    private String destination_country;
    private String shipping_type;
    private double weight;
    private int length;
    private int width;
    private int height;
    private String insurance;
    private double item_value;
}
