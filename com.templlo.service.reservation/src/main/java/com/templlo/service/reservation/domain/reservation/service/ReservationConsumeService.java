package com.templlo.service.reservation.domain.reservation.service;

import com.google.gson.Gson;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationException;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.repository.ReservationRepository;
import com.templlo.service.reservation.domain.reservation.service.model.consume.CreateReservationResultConsume;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.templlo.service.reservation.domain.reservation.service.model.consume.ConsumerTopicGroupName.GROUP_RESERVATION_CONFIRM;
import static com.templlo.service.reservation.domain.reservation.service.model.consume.ConsumerTopicGroupName.TOPIC_RESERVATION_CONFIRM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationConsumeService {
    public final ReservationRepository reservationRepository;
    private final Gson gson;

    @KafkaListener(groupId = GROUP_RESERVATION_CONFIRM, topics = TOPIC_RESERVATION_CONFIRM)
    @Transactional
    public void createReservationResultConsumer(String messageStr) {
        CreateReservationResultConsume message = gson.fromJson(messageStr, CreateReservationResultConsume.class);

        Reservation reservation = reservationRepository.findById(message.reservationId()).orElseThrow(
                () -> new ReservationException(ReservationStatusCode.RESERVATION_NOT_FOUND));

        switch (message.status()) {
            case SUCCESS -> reservation.updateStatusCompleted();
            case FAILURE -> reservation.updateStatusFailed();
        }
    }
}
