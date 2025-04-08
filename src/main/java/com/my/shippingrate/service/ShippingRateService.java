package com.my.shippingrate.service;

import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.RateDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;

import java.util.List;

public interface ShippingRateService {
    ResponseWrapperDTO calculateShippingRate(PayloadDTO payloadDTO, List<RateDTO> rateDTOList);
    String getProvider();
}
