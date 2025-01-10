package com.templlo.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.coupon.service.CouponService;
import com.templlo.service.kafka.dto.UserLevelUpEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLevelUpConsumer {

	private final CouponService couponService; // 쿠폰 발급을 위한 서비스 의존성
	private final ObjectMapper objectMapper;  // JSON 변환용

	@KafkaListener(topics = "userLevelUp", groupId = "coupon-service-group")
	public void handleUserLevelUpEvent(String message) {
		try {
			// 메시지 파싱
			UserLevelUpEvent event = objectMapper.readValue(message, UserLevelUpEvent.class);
			log.info("Received userLevelUp event: {}", event);

			// 쿠폰 발급 요청
			couponService.issueLevelUpCoupon(event.userId());

		} catch (Exception e) {
			log.error("Failed to process userLevelUp event: {}", message, e);
		}
	}
}
