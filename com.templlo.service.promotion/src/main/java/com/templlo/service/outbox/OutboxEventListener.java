package com.templlo.service.outbox;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.templlo.service.outbox.entity.OutboxMessage;
import com.templlo.service.outbox.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventListener {

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final OutboxRepository outboxRepository;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleOutboxEvent(OutboxEvent event) {
		log.info("OutboxEvent 수신: EventType={}, Payload={}", event.getEventType(), event.getPayload());

		OutboxMessage outboxMessage = outboxRepository.findByEventTypeAndPayload(
			event.getEventType(), event.getPayload()
		).orElseThrow(() -> new IllegalStateException("OutboxMessage를 찾을 수 없습니다."));

		kafkaTemplate.send(event.getEventType(), event.getPayload())
			.whenComplete((result, ex) -> {
				if (ex != null) {
					updateOutboxStatus(outboxMessage, "FAILED");
					log.error("Kafka 메시지 발행 실패: EventType={}, Payload={}, Error={}",
						event.getEventType(), event.getPayload(), ex.getMessage());
				} else {
					updateOutboxStatus(outboxMessage, "SENT");
					log.info("Kafka 메시지 발행 성공: EventType={}, Payload={}", event.getEventType(), event.getPayload());
				}
			});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateOutboxStatus(OutboxMessage outboxMessage, String status) {
		log.info("OutboxMessage 상태 업데이트 시작: id={}, 이전 상태={}, 새로운 상태={}",
			outboxMessage.getId(), outboxMessage.getStatus(), status);
		outboxMessage.updateStatus(status);
		outboxRepository.save(outboxMessage);
		log.info("OutboxMessage 상태 업데이트 완료: id={}, 새로운 상태={}", outboxMessage.getId(), status);
	}
}
