package com.my.shippingrate.service.redis;

import com.my.shippingrate.dto.response.ResponseWrapperDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, ResponseWrapperDTO> redisTemplate;

    @Override
    public ResponseWrapperDTO getValueByCacheKey(String cacheKey) {
        return redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public void saveCache(ResponseWrapperDTO responseWrapperDTO, String cacheKey) {
        redisTemplate.opsForValue().set(cacheKey, responseWrapperDTO, Duration.ofMinutes(10));
    }

}
