package com.templlo.service.temple.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisCacheEvictor {

    private final RedisTemplate<String, Object> redisTemplate;

    public void evictCachesByPattern(String pattern) {
        // Redis 키 패턴 검색
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
