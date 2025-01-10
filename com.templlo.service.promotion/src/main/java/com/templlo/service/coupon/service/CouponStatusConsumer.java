package com.templlo.service.coupon.service;

import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.templlo.service.coupon.dto.CouponStatusEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponStatusConsumer {

	private final CacheManager cacheManager;

	@KafkaListener(topics = "coupon-status-topic", groupId = "coupon-service-group")
	public void updateCouponStatusCache(CouponStatusEvent event) {
		// Redis 캐시 업데이트
		cacheManager.getCache("couponStatus").put(event.promotionId(), event);
	}
}
