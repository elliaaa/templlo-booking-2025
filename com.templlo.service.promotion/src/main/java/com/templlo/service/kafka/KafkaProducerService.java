package com.templlo.service.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.templlo.service.coupon.dto.CouponIssueRequestDto;
import com.templlo.service.coupon.dto.CouponIssuedResponseEvent;
import com.templlo.service.coupon.dto.LevelUpCouponEvent;
import com.templlo.service.coupon.dto.TicketApplyRequestDto;
import com.templlo.service.promotion.dto.PromotionCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	private static final String COUPON_ISSUE_TOPIC = "coupon-issue-topic";
	private static final String COUPON_ISSUED_RESPONSE_TOPIC = "coupon-issued-response-topic";

	/**
	 * 프로모션 생성 이벤트 전송
	 */
	public void sendPromotionCreatedEvent(PromotionCreatedEvent event) {
		String topic = "promotion-created-topic";
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event);

		future.whenComplete((result, ex) -> {
			if (ex != null) {
				log.error("Failed to send PromotionCreatedEvent to topic '{}': {}", topic, ex.getMessage(), ex);
			} else {
				log.info("PromotionCreatedEvent sent successfully to topic '{}': {}", topic, event);
			}
		});
	}

	/**
	 * 쿠폰 발급 요청 이벤트 전송
	 */
	public void sendCouponIssueRequest(CouponIssueRequestDto request, String loginId) {
		try {
			kafkaTemplate.send(
				MessageBuilder.withPayload(request)
					.setHeader("X-Login-Id", loginId) // 헤더 추가
					.setHeader(KafkaHeaders.TOPIC, COUPON_ISSUE_TOPIC) // 토픽 추가
					.build()
			);
			log.info("Coupon issue request sent successfully to topic '{}': {}, X-Login-Id: {}", COUPON_ISSUE_TOPIC,
				request, loginId);
		} catch (Exception e) {
			log.error("Failed to send coupon issue request: {}", e.getMessage(), e);
		}
	}

	/**
	 * 쿠폰 발급 응답 이벤트 전송
	 */
	public void sendCouponIssueResponse(CouponIssuedResponseEvent responseEvent) {
		try {
			kafkaTemplate.send(
				MessageBuilder.withPayload(responseEvent)
					.setHeader(KafkaHeaders.TOPIC, COUPON_ISSUED_RESPONSE_TOPIC) // 토픽 추가
					.build()
			);
			log.info("Coupon issued response sent successfully to topic '{}': {}", COUPON_ISSUED_RESPONSE_TOPIC,
				responseEvent);
		} catch (Exception e) {
			log.error("Failed to send coupon issued response: {}", e.getMessage(), e);
		}
	}

	/**
	 * 레벨업 쿠폰 발급 이벤트 전송
	 */
	public void sendLevelUpCouponEvent(LevelUpCouponEvent event) {
		String topic = "level-up-coupon-topic";
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event);

		future.whenComplete((result, ex) -> {
			if (ex != null) {
				log.error("Failed to send LevelUpCouponEvent to topic '{}': {}", topic, ex.getMessage(), ex);
			} else {
				log.info("LevelUpCouponEvent sent successfully to topic '{}': {}", topic, event);
			}
		});
	}

	public void sendToQueue(TicketApplyRequestDto ticketApplyRequest) {
		String topic = "ticket-application-queue";
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, ticketApplyRequest);

		future.whenComplete((result, ex) -> {
			if (ex != null) {
				log.error("Failed to send message to topic '{}': {}", topic, ex.getMessage(), ex);
			} else {
				log.info("Message sent successfully to topic '{}': {}", topic, ticketApplyRequest);
			}
		});
	}

	/**
	 * Kafka 메시지 전송 공통 메서드
	 */
	public void sendMessageWithCallback(String topic, Object message) {
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);

		future.whenComplete((result, ex) -> {
			if (ex != null) {
				log.error("Failed to send message to topic '{}': {}", topic, ex.getMessage(), ex);
			} else {
				log.info("Message sent successfully to topic '{}': {}", topic, result.getProducerRecord().value());
			}
		});
	}

	public void sendMessageWithHeader(String topic, Object payload, String headerKey, String headerValue) {
		kafkaTemplate.send(
			MessageBuilder.withPayload(payload)
				.setHeader(headerKey, headerValue)
				.build()
		);
	}

}
