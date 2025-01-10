package com.templlo.service.coupon.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.coupon.dto.CouponIssuedEvent;
import com.templlo.service.coupon.dto.TicketApplyRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final ObjectMapper objectMapper;

	// 쿠폰 발행 이벤트 처리
	@KafkaListener(
		topics = "coupon-issued-topic",
		groupId = "coupon-consumer-group",
		containerFactory = "kafkaListenerContainerFactory"
	)
	public void processCouponIssuedEvent(String message, Acknowledgment acknowledgment) {
		try {
			// JSON 메시지를 DTO로 변환
			CouponIssuedEvent event = objectMapper.readValue(message, CouponIssuedEvent.class);

			// 쿠폰 발행 로직 추가 (예: 데이터베이스 저장, 로그 처리 등)
			System.out.printf("Processed CouponIssuedEvent: %s%n", event);

			// 메시지 확인 (ACK)
			acknowledgment.acknowledge();

		} catch (Exception e) {
			// 예외 처리
			System.err.printf("Failed to process CouponIssuedEvent: %s%n", e.getMessage());
		}
	}

	@KafkaListener(topics = "ticket-application-queue", groupId = "ticket-service")
	public void consumeTicketRequest(TicketApplyRequestDto ticketApplyRequest) {
		// 대기열 등록 처리 로직
		System.out.println("Received ticket application: " + ticketApplyRequest);
	}

}
