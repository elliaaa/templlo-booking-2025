package com.templlo.service.common.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.outbox.OutboxEvent;
import com.templlo.service.outbox.entity.OutboxMessage;
import com.templlo.service.outbox.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherAspect {

	private final OutboxRepository outboxRepository;
	private final ApplicationEventPublisher eventPublisher;

	private final ExpressionParser expressionParser = new SpelExpressionParser();
	private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 인스턴스 추가

	@AfterReturning(value = "@annotation(outboxPublisher)", returning = "response")
	public void publishOutboxEvent(JoinPoint joinPoint, OutboxPublisher outboxPublisher, Object response) {
		String eventType = outboxPublisher.eventType();
		String payloadExpression = outboxPublisher.payloadExpression();

		try {
			// SpEL 평가 컨텍스트 생성
			EvaluationContext context = new StandardEvaluationContext();
			context.setVariable("response", response);
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				context.setVariable("arg" + i, joinPoint.getArgs()[i]);
			}

			// SpEL 표현식 평가
			Object extractedPayload = expressionParser.parseExpression(payloadExpression).getValue(context);

			// ObjectMapper로 JSON 직렬화
			String payload = objectMapper.writeValueAsString(extractedPayload);

			// OutboxMessage 생성 및 저장
			OutboxMessage outboxMessage = OutboxMessage.builder()
				.eventType(eventType)
				.payload(payload)
				.status("PENDING")
				.createdAt(LocalDateTime.now())
				.build();

			outboxRepository.save(outboxMessage);

			// 이벤트 발행
			eventPublisher.publishEvent(OutboxEvent.builder()
				.eventType(eventType)
				.payload(payload)
				.timestamp(LocalDateTime.now())
				.build());

			log.info("Outbox 이벤트 발행 완료: EventType={}, Payload={}", eventType, payload);

		} catch (Exception e) {
			log.error("OutboxPublisherAspect 처리 중 오류 발생: EventType={}, Error={}", eventType, e.getMessage(), e);
			throw new RuntimeException("Outbox 이벤트 발행 실패", e);
		}
	}
}
