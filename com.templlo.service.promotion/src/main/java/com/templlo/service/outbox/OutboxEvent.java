package com.templlo.service.outbox;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutboxEvent {
	private final String eventType;
	private final String payload;
	private final LocalDateTime timestamp;
}
