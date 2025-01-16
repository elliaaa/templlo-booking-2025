package com.templlo.service.common.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.templlo.service.outbox.OutboxEvent;
import com.templlo.service.outbox.entity.OutboxMessage;
import com.templlo.service.outbox.repository.OutboxRepository;
import com.templlo.service.promotion.dto.PromotionResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherAspect {

	private final OutboxRepository outboxRepository;
	private final ApplicationEventPublisher eventPublisher;

	@AfterReturning(value = "@annotation(outboxPublisher)", returning = "response")
	public void publishOutboxEvent(JoinPoint joinPoint, OutboxPublisher outboxPublisher, Object response) {
		String eventType = outboxPublisher.eventType();

		// 메서드 결과에서 Promotion ID 추출 (PromotionResponseDto 기준)
		if (response instanceof PromotionResponseDto dto) {
			String payload = String.format("{\"promotionId\":\"%s\",\"message\":\"%s\"}",
				dto.promotionId(), dto.message());

			// OutboxMessage 생성 및 저장
			OutboxMessage outboxMessage = OutboxMessage.builder()
				.eventType(eventType)
				.payload(payload)
				.status("PENDING")
				.createdAt(LocalDateTime.now())
				.build();
			outboxRepository.save(outboxMessage);

			// Spring 이벤트 발행
			eventPublisher.publishEvent(OutboxEvent.builder()
				.eventType(eventType)
				.payload(payload)
				.timestamp(LocalDateTime.now())
				.build());

			log.info("Outbox 이벤트 발행 완료: EventType={}, Payload={}", eventType, payload);
		}
	}
}
