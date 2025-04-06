package com.my.shippingrate.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "provider",  // This will be used to identify the type
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JntRequestDTO.class, name = "JNT"),
        @JsonSubTypes.Type(value = CityLinkRequestDTO.class, name = "CITYLINK")
})
@Data
public abstract class PayloadDTO {
    private String provider;
}
