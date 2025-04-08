package com.my.shippingrate.dto.request.citylink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.shippingrate.dto.request.PayloadDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "CityLink Express Shipping Rate Request")
@JsonIgnoreProperties(value = "provider", allowGetters = false, allowSetters = true)
public class CityLinkRequestDTO extends PayloadDTO {
    @JsonProperty("origin_country")
    private String originCountry;

    @JsonProperty("origin_state")
    private String originState;

    @JsonProperty("origin_postcode")
    private String originPostcode;

    @JsonProperty("destination_country")
    private String destinationCountry;

    @JsonProperty("destination_state")
    private String destinationState;

    @JsonProperty("destination_postcode")
    private String destinationPostcode;

    private double length;
    private double width;
    private double height;

    @JsonProperty("selected_type")
    private int selectedType;

    @JsonProperty("parcel_weight")
    private double parcelWeight;

    @JsonProperty("document_weight")
    private double documentWeight;
}
