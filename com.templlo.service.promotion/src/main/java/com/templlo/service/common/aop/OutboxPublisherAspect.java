package com.templlo.service.common.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

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

	private final ExpressionParser parser = new SpelExpressionParser();

	@AfterReturning(value = "@annotation(outboxPublisher)", returning = "response")
	public void publishOutboxEvent(JoinPoint joinPoint, OutboxPublisher outboxPublisher, Object response) {
		String eventType = outboxPublisher.eventType();
		String payloadExpression = outboxPublisher.payloadExpression();

		// SpEL 컨텍스트 설정
		StandardEvaluationContext context = new StandardEvaluationContext();
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			context.setVariable("arg" + i, args[i]); // 메서드 파라미터를 arg0, arg1, ...로 설정
		}
		context.setVariable("response", response); // 메서드 반환값을 response로 설정

		// SpEL 표현식 파싱
		String payload = parser.parseExpression(payloadExpression).getValue(context, String.class);

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
