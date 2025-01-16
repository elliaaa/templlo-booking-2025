package com.templlo.service.outbox.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox")
@Getter
@NoArgsConstructor
public class OutboxMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String eventType;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String payload;

	@Column(nullable = false)
	private String status; // PENDING, SENT, FAILED

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

	@Builder
	public OutboxMessage(String eventType, String payload, String status, LocalDateTime createdAt) {
		this.eventType = eventType;
		this.payload = payload;
		this.status = status;
		this.createdAt = createdAt;
	}

	public void updateStatus(String status) {
		this.status = status;
		this.updatedAt = LocalDateTime.now();
	}
}
