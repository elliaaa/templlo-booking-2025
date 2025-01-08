package com.templlo.service.coupon.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.templlo.service.coupon.dto.CouponStatusEvent;
import com.templlo.service.coupon.dto.TicketApplyRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j // Slf4j 로그 추가
public class KafkaProducerService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * 쿠폰 발급 이벤트 전송
	 */
	// public void sendCouponIssuedEvent(UserCoupon userCoupon) {
	// 	var event = new CouponIssuedEvent(
	// 		userCoupon.getCoupon().getCouponId().toString(),
	// 		userCoupon.getUserId().toString(), // UserCoupon의 userId 사용
	// 		userCoupon.getCoupon().getPromotion().getPromotionId().toString(),
	// 		null, // programId 필요 시 설정
	// 		userCoupon.getCoupon().getGender(),
	// 		"ISSUED"
	// 	);
	//
	// 	sendMessageWithCallback("coupon-issued-topic", event);
	// }

	/**
	 * 티켓 신청 요청 전송
	 */
	public void sendToQueue(TicketApplyRequestDto ticketApplyRequest) {
		sendMessageWithCallback("ticket-application-queue", ticketApplyRequest);
	}

	/**
	 * 쿠폰 상태 이벤트 전송
	 */
	public void sendCouponStatusEvent(CouponStatusEvent event) {
		sendMessageWithCallback("coupon-status-topic", event);
	}

	/**
	 * Kafka 메시지 전송 공통 메서드
	 */
	private void sendMessageWithCallback(String topic, Object message) {
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);

		future.whenComplete((result, ex) -> {
			if (ex != null) {
				// 실패 시 로그 출력
				log.error("Failed to send message to topic '{}': {}", topic, ex.getMessage(), ex);
			} else {
				// 성공 시 로그 출력
				log.info("Message sent successfully to topic '{}': {}", topic, result.getProducerRecord().value());
			}
		});
	}
}
