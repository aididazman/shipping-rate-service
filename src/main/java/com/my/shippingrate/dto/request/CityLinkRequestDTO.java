package com.my.shippingrate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "CityLink Express Shipping Rate Request")
public class CityLinkRequestDTO extends PayloadDTO {
    private String city_address;
}
