package com.my.shippingrate.service;

import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.utils.ProviderType;
import org.springframework.stereotype.Service;

@Service
public class JntServiceImpl implements ShippingRateService {
    @Override
    public ResponseWrapperDTO fetchShippingRate(PayloadDTO payloadDTO) {
        return null;
    }

    @Override
    public String getProvider() {
        return ProviderType.JNT.name();
    }
}
