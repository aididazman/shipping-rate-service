package com.my.shippingrate.dto.request.jnt;

import com.my.shippingrate.dto.request.PayloadDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "J&T Shipping Rate Request")
public class JntRequestDTO extends PayloadDTO {

    @NotNull(message = "Shipping rates type is required")
    @JsonProperty("shipping_rates_type")
    private String shippingRatesType;

    @NotNull(message = "Sender postcode is required")
    @JsonProperty("sender_postcode")
    private String senderPostcode;

    @NotNull(message = "Receiver postcode is required")
    @JsonProperty("receiver_postcode")
    private String receiverPostcode;

    @JsonProperty("destination_country")
    private String destinationCountry;

    @NotNull(message = "Shipping type is required")
    @JsonProperty("shipping_type")
    private String shippingType;

    @NotNull(message = "Weight is required")
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
    private Double itemValue = 0.0;
}
