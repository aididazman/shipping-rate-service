package com.my.shippingrate.service;

import com.my.shippingrate.dto.request.citylink.CityLinkRequestDTO;
import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.RateDTO;
import com.my.shippingrate.dto.response.citylink.CityLinkResponseDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.utils.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CityLinkServiceImpl implements ShippingRateService {

    private final WebClient webClient;

    @Value("${citylink.api.shipping-rate.url}")
    private String cityLinkUrl;

    public CityLinkServiceImpl(@Qualifier("cityLinkWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResponseWrapperDTO calculateShippingRate(PayloadDTO payloadDTO, List<RateDTO> rateDTOList) {

        CityLinkRequestDTO request = (CityLinkRequestDTO) payloadDTO;

        CityLinkResponseDTO response = webClient.post()
                .uri(cityLinkUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CityLinkResponseDTO.class)
                .block();

        if (response == null || response.getReq() == null || response.getReq().getData() == null) {
            throw new IllegalStateException("Incomplete response from City-Link");
        }

        BigDecimal rate = BigDecimal.valueOf(response.getReq().getData().getRate());
        RateDTO rateDTO =  new RateDTO(ProviderType.CITYLINK.getCourier(), rate);
        rateDTOList.add(rateDTO);

        ResponseWrapperDTO responseWrapperDTO = new ResponseWrapperDTO(rateDTOList);
        log.info("Complete calculate shipping rate with response: {}",responseWrapperDTO );
        return responseWrapperDTO;
    }

    @Override
    public String getProvider() {
        return ProviderType.CITYLINK.name();
    }
}
