package com.my.shippingrate.controller;

import com.my.shippingrate.dto.request.ProviderDTO;
import com.my.shippingrate.dto.response.RatesDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import io.swagger.v3.oas.annotations.Operation;
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

    //@TODO add logic to process request bodies
    @Operation( summary = "Calculate shipping rates", tags = { "shipping-rate" })
    @PostMapping("/rates")
    public ResponseEntity<ResponseWrapperDTO> calculateShippingRates(@RequestBody List<ProviderDTO> providerListDTO) {
        List<RatesDTO> ratesList = new ArrayList<>();
        ResponseWrapperDTO data = new ResponseWrapperDTO(ratesList);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }



}
