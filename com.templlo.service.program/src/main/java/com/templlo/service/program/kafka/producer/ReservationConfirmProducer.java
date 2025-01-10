package com.templlo.service.program.kafka.producer;

import com.templlo.service.program.kafka.message.reservation.ReservationConfirmMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReservationConfirmProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationConfirmProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String reservationConfirmedTopic, ReservationConfirmMessage message) {
        log.info("Produce ReservationConfirmed Message start");
        kafkaTemplate.send(reservationConfirmedTopic, message);
        log.info("Produce ReservationConfirmed Message end");
    }

}
