package com.templlo.service.outbox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.templlo.service.outbox.entity.OutboxMessage;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
	Optional<OutboxMessage> findByEventTypeAndPayload(String eventType, String payload);
}
