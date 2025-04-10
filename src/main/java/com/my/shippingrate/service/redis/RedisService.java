package com.my.shippingrate.service.redis;

import com.my.shippingrate.dto.response.ResponseWrapperDTO;

public interface RedisService {

    ResponseWrapperDTO getValueByCacheKey(String cacheKey);
    void saveCache(ResponseWrapperDTO responseWrapperDTO, String cacheKey);
}
