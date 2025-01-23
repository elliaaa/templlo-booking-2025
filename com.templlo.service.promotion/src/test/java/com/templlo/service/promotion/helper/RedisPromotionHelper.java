package com.templlo.service.promotion.helper;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component
public class RedisPromotionHelper {

	private static final Logger log = LoggerFactory.getLogger(RedisPromotionHelper.class);
	private final RedisTemplate<String, Object> redisTemplate;

	public RedisPromotionHelper(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void initializeRedisPromotionCounters(UUID promotionId, int totalCoupons) {
		String remainingKey = "promotion:" + promotionId + ":remaining";

		if (redisTemplate.opsForValue().get(remainingKey) == null) {
			redisTemplate.opsForValue().set(remainingKey, (long)totalCoupons);
			log.debug("Redis 초기화: {} -> {}", remainingKey, totalCoupons);
		}
	}

	public <T> T executeScript(RedisScript<T> script, List<String> keys, Object... args) {
		return redisTemplate.execute(script, keys, args);
	}
}
