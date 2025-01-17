package com.templlo.service.program.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.templlo.service.program.kafka.message.reservation.ReservationConfirmMessage;
import com.templlo.service.program.kafka.message.reservation.ReservationCreateMessage;
import com.templlo.service.program.kafka.producer.ReservationConfirmProducer;
import com.templlo.service.program.kafka.service.ReservationConfirmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConsumer {

    private final ReservationConfirmProducer reservationConfirmProducer;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;
    private final ReservationConfirmService reservationConfirmService;

    @Value("${spring.kafka.topics.reservation-confirmed}")
    private String reservationConfirmedTopic;


    // 예약 생성 메시지
    @KafkaListener(topics = "${spring.kafka.topics.reservation-created}", groupId = "reservation-created-program")
    public void consumeReservationCreated(String reservationCreatedMessage) throws Exception {

        ReservationCreateMessage message = objectMapper.readValue(reservationCreatedMessage, ReservationCreateMessage.class);

        String lockKey = "programLock:" + message.programId();
        RLock lock = redissonClient.getLock(lockKey);

        ReservationConfirmMessage reservationConfirmMessage = null;

        try {
            if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                reservationConfirmMessage = reservationConfirmService.processReservation(message);
            } else {
                log.error("Failed to acquire lock for programId: {}", message.programId());
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            if (reservationConfirmMessage != null) {
                reservationConfirmProducer.send(reservationConfirmedTopic, reservationConfirmMessage);
            }
        }


    }

}