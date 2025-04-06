package com.my.shippingrate.controller;

import com.my.shippingrate.dto.request.CityLinkRequestDTO;
import com.my.shippingrate.dto.request.JntRequestDTO;
import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.RatesDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.service.ShippingRateFactory;
import com.my.shippingrate.service.ShippingRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/shipping")
public class ShippingRateController {

    private final ShippingRateFactory shippingRateFactory;

    public ShippingRateController(ShippingRateFactory shippingRateFactory) {
        this.shippingRateFactory = shippingRateFactory;
    }

    //@TODO add logic to process request bodies
    @Operation( summary = "Calculate shipping rates", tags = { "shipping-rate" })
    @PostMapping("/rates")
    public ResponseEntity<ResponseWrapperDTO> fetchShippingRate(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Polymorphic request body for different providers",
            required = true,
            content = @Content(
                    schema = @Schema(oneOf = { JntRequestDTO.class, CityLinkRequestDTO.class }),
                    examples = {
                            @ExampleObject(
                                    name = "J&T Request",
                                    summary = "Example request for J&T",
                                    value = "{\n" +
                                            "  \"provider\": \"JNT\",\n" +
                                            "  \"_token\": \"abc123\",\n" +
                                            "  \"shipping_rates_type\": \"domestic\",\n" +
                                            "  \"sender_postcode\": \"43000\",\n" +
                                            "  \"receiver_postcode\": \"43300\",\n" +
                                            "  \"destination_country\": \"BWN\",\n" +
                                            "  \"shipping_type\": \"EZ\",\n" +
                                            "  \"weight\": 11,\n" +
                                            "  \"length\": 1,\n" +
                                            "  \"width\": 1,\n" +
                                            "  \"height\": 1,\n" +
                                            "  \"insurance\": \"\",\n" +
                                            "  \"item_value\": 111\n" +
                                            "}"
                            ),
                            @ExampleObject(
                                    name = "CityLink Request",
                                    summary = "Example request for CityLink",
                                    value = "{\n" +
                                            "  \"provider\": \"CITYLINK\",\n" +
                                            "  \"token\": \"xyz456\",\n" +
                                            "  \"origin_postcode\": \"50000\",\n" +
                                            "  \"destination_postcode\": \"56000\",\n" +
                                            "  \"weight\": 5\n" +
                                            "}"
                            )
                    }
            )
    ) @RequestBody PayloadDTO request) {
        log.info("Request to fetch shipping rate for : {}", request.getProvider());
        ShippingRateService service = shippingRateFactory.getService(request.getProvider());
        return new ResponseEntity<>(service.fetchShippingRate(request), HttpStatus.OK);
    }



}
