package com.my.shippingrate.dto.request.citylink;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.shippingrate.dto.request.PayloadDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "CityLink Express Shipping Rate Request")
@JsonIgnoreProperties(value = "provider", allowGetters = false, allowSetters = true)
public class CityLinkRequestDTO extends PayloadDTO {

    @NotNull(message = "Origin country is required")
    @JsonProperty("origin_country")
    private String originCountry;

    @NotNull(message = "Origin state is required")
    @JsonProperty("origin_state")
    private String originState;

    @NotNull(message = "Origin postcode is required")
    @JsonProperty("origin_postcode")
    private String originPostcode;

    @NotNull(message = "Destination country is required")
    @JsonProperty("destination_country")
    private String destinationCountry;

    @NotNull(message = "Destination state is required")
    @JsonProperty("destination_state")
    private String destinationState;

    @NotNull(message = "Destination postcode is required")
    @JsonProperty("destination_postcode")
    private String destinationPostcode;

    @NotNull(message = "Length is required")
    private Double length;

    @NotNull(message = "Width is required")
    private Double width;

    @NotNull(message = "Height is required")
    private Double height;

    @NotNull(message = "Height is required")
    @JsonProperty("selected_type")
    private Integer selectedType;

    @DecimalMax(value = "340", message = "Parcel weight should not be greater than 340")
    @JsonProperty("parcel_weight")
    private Double parcelWeight;

    @DecimalMax(value = "340", message = "Parcel weight should not be greater than 340")
    @JsonProperty("document_weight")
    private Double documentWeight;
}
