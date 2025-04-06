package com.my.shippingrate.service;

import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;

public interface ShippingRateService {
    ResponseWrapperDTO fetchShippingRate(PayloadDTO payloadDTO);
    String getProvider();
}
