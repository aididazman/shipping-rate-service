package com.my.shippingrate.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CityLinkRequestDTO extends PayloadDTO {
    private String city_address;
}
