package com.templlo.service.promotion.helper;

import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.script.RedisScript;

public class MockRedisPromotionHelper extends RedisPromotionHelper {

	public MockRedisPromotionHelper() {
		super(null); // 실제 RedisTemplate을 사용하지 않음
	}

	@Override
	public void initializeRedisPromotionCounters(UUID promotionId, int totalCoupons) {
		System.out.printf("[테스트] Redis 초기화: %s -> %d%n", promotionId, totalCoupons);
	}

	@Override
	public <T> T executeScript(RedisScript<T> script, List<String> keys, Object... args) {
		// 테스트 환경에서는 항상 성공했다고 가정
		return (T)Long.valueOf(1);
	}
}
