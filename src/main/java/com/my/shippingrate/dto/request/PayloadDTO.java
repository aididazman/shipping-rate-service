package com.my.shippingrate.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.my.shippingrate.dto.request.citylink.CityLinkRequestDTO;
import com.my.shippingrate.dto.request.jnt.JntRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "provider",  // This will be used to identify the type
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JntRequestDTO.class, name = "JNT"),
        @JsonSubTypes.Type(value = CityLinkRequestDTO.class, name = "CITYLINK")
})
@Data
public abstract class PayloadDTO {

    @NotBlank
    @NotNull(message = "Provider cannot be null")
    private String provider;
}
