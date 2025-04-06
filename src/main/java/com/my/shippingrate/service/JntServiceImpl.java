package com.my.shippingrate.service;

import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.utils.ProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class JntServiceImpl implements ShippingRateService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public ResponseWrapperDTO fetchShippingRate(PayloadDTO payloadDTO) {
        return null;
    }

    @Override
    public String getProvider() {
        return ProviderType.JNT.name();
    }
}
