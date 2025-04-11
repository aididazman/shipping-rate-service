package com.my.shippingrate.controller;

import com.my.shippingrate.dto.request.citylink.CityLinkRequestDTO;
import com.my.shippingrate.dto.request.jnt.JntRequestDTO;
import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.RateDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.service.ShippingRateFactory;
import com.my.shippingrate.service.ShippingRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ShippingRateController {

    private final ShippingRateFactory shippingRateFactory;

    @Operation( summary = "Calculate shipping rates", tags = { "shipping-rate" })
    @PostMapping("v1/shipping/rates")
    public ResponseEntity<ResponseWrapperDTO> calculateShippingRate(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Polymorphic request body for different providers",
            required = true,
            content = @Content(
                    schema = @Schema(oneOf = { JntRequestDTO.class, CityLinkRequestDTO.class }),
                    examples = {
                            @ExampleObject(
                                    name = "J&T Request",
                                    summary = "Example request for J&T",
                                    value = """
                                        {
                                            "provider": "JNT",
                                            "shipping_rates_type": "domestic",
                                            "sender_postcode": "43000",
                                            "receiver_postcode": "43300",
                                            "destination_country": "BWN",
                                            "shipping_type": "EZ",
                                            "weight": 11,
                                            "length": 1,
                                            "width": 1,
                                            "height": 1,
                                            "insurance": "",
                                            "item_value": 111
                                        }
                                    """
                            ),
                            @ExampleObject(
                                    name = "CityLink Request",
                                    summary = "Example request for CityLink",
                                    value = """
                                        {
                                            "provider": "CITYLINK",
                                            "origin_country": "MY",
                                            "origin_state": "Selangor",
                                            "origin_postcode": "43000",
                                            "destination_country": "MY",
                                            "destination_state": "Selangor",
                                            "destination_postcode": "43300",
                                            "length": 10,
                                            "width": 10,
                                            "height": 10,
                                            "selected_type": 1,
                                            "parcel_weight": 10,
                                            "document_weight": 0
                                        }
                                    """
                            )
                    }
            )
    ) @RequestBody @Valid PayloadDTO request) {
        log.info("Request to fetch shipping rate for : {}", request.getProvider());
        ShippingRateService service = shippingRateFactory.getService(request.getProvider());
        List<RateDTO> rateDTOList = new ArrayList<>();
        return new ResponseEntity<>(service.calculateShippingRate(request, rateDTOList), HttpStatus.OK);
    }

}
