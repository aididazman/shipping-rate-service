package com.my.shippingrate.dto.request.jnt;

import com.my.shippingrate.dto.request.PayloadDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "J&T Shipping Rate Request")
public class JntRequestDTO extends PayloadDTO {
    private String token;
    private String shippingRatesType;
    private String senderPostcode;
    private String receiverPostcode;
    private String destinationCountry;
    private String shippingType;
    private double weight;
    private int length;
    private int width;
    private int height;
    private String insurance;
    private double itemValue;
}
