package com.templlo.service.program.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.program.kafka.message.reservation.ReservationConfirmMessage;
import com.templlo.service.program.kafka.message.reservation.ReservationConfirmMessageStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ReservationCancelProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAX_RETRY_COUNT = 3;

    public ReservationCancelProducer(KafkaTemplate<String, Object> kafkaTemplate, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
    }

    public void send(String reservationCancelConfirmedTopic, ReservationConfirmMessage message) {

        String reservationConfirmKey = "canceledReservationId:" + message.reservationId();

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(reservationCancelConfirmedTopic, message);

        // 전송 성공/실패 처리
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully for reservationId: {}", message.reservationId());
                redisTemplate.delete(reservationConfirmKey);
            } else {
                // 실패 시 Redis 상태를 failure로 업데이트 및 재시도 로직
                log.error("Failed to send message for reservationId: {}. Error: {}", message.reservationId(), ex.getMessage());
                handleSendFailure(reservationCancelConfirmedTopic, message, reservationConfirmKey);
            }
        });
    }

    private void handleSendFailure(String reservationConfirmedTopic, ReservationConfirmMessage message, String reservationConfirmKey) {

        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        for (int attempt = 1; attempt <= MAX_RETRY_COUNT; attempt++) {
            try {
                // Kafka 전송 재시도
                kafkaTemplate.send(reservationConfirmedTopic, message);
                log.info("Retry successful for reservationId: {} on attempt {}", message.reservationId(), attempt);

                // 성공 시 Redis 상태를 success로 업데이트하고 종료
                hashOps.put(reservationConfirmKey, "status", ReservationConfirmMessageStatus.CONFIRMED);
                redisTemplate.expire(reservationConfirmKey, Duration.ofMinutes(1));
                return;
            } catch (Exception retryEx) {
                log.warn("Retry {} failed for reservationId: {}. Error: {}", attempt, message.reservationId(), retryEx.getMessage());
            }
        }
        // 최대 재시도 횟수 초과 시 상태를 failure로 업데이트
        hashOps.put(reservationConfirmKey, "status", ReservationConfirmMessageStatus.FAILED);
        redisTemplate.expire(reservationConfirmKey, Duration.ofHours(24));
    }


}
