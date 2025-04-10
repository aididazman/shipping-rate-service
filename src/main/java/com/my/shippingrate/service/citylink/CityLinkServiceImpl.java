package com.my.shippingrate.service.citylink;

import com.my.shippingrate.dto.request.citylink.CityLinkRequestDTO;
import com.my.shippingrate.dto.request.PayloadDTO;
import com.my.shippingrate.dto.response.RateDTO;
import com.my.shippingrate.dto.response.citylink.CityLinkResponseDTO;
import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import com.my.shippingrate.service.ShippingRateService;
import com.my.shippingrate.service.redis.RedisService;
import com.my.shippingrate.utils.CommonUtil;
import com.my.shippingrate.utils.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CityLinkServiceImpl implements ShippingRateService {

    private final WebClient webClient;
    private final RedisService redisService;

    @Value("${citylink.api.shipping-rate.url}")
    private String cityLinkUrl;

    public CityLinkServiceImpl(@Qualifier("cityLinkWebClient") WebClient webClient, RedisService redisService) {
        this.webClient = webClient;
        this.redisService = redisService;
    }

    @Override
    public ResponseWrapperDTO calculateShippingRate(PayloadDTO payloadDTO, List<RateDTO> rateDTOList) {

        CityLinkRequestDTO request = (CityLinkRequestDTO) payloadDTO;

        String cacheKey = CommonUtil.generateCacheKey(request);
        log.info("Generated cache key : {}", cacheKey);
        ResponseWrapperDTO cachedResponse = redisService.getValueByCacheKey(cacheKey);

        if (cachedResponse != null) {
            log.info("Fetch shipping rate from cache");
            return cachedResponse;
        }

        return send(request, rateDTOList, cacheKey);
    }

    private ResponseWrapperDTO send(CityLinkRequestDTO request, List<RateDTO> rateDTOList, String cacheKey) {

        log.info("Calling CityLink URL: {}", cityLinkUrl);
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
        log.info("Saving response to redis for caching purpose");
        redisService.saveCache(responseWrapperDTO, cacheKey);

        log.info("Complete calculate shipping rate with response: {}",responseWrapperDTO );
        return responseWrapperDTO;
    }

    @Override
    public String getProvider() {
        return ProviderType.CITYLINK.name();
    }
}
