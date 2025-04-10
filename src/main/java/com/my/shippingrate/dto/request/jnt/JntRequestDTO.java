package com.my.shippingrate.dto.request.jnt;

import com.my.shippingrate.dto.request.PayloadDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "J&T Shipping Rate Request")
public class JntRequestDTO extends PayloadDTO {

    @JsonProperty("shipping_rates_type")
    private String shippingRatesType;

    @JsonProperty("sender_postcode")
    private String senderPostcode;

    @JsonProperty("receiver_postcode")
    private String receiverPostcode;

    @JsonProperty("destination_country")
    private String destinationCountry;

    @JsonProperty("shipping_type")
    private String shippingType;

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("length")
    private Double length;

    @JsonProperty("width")
    private Double width;

    @JsonProperty("height")
    private Double height;

    @JsonProperty("insurance")
    private String insurance;

    @JsonProperty("item_value")
    private Double itemValue;
}
