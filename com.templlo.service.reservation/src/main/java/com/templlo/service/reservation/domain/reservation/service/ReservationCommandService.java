package com.templlo.service.reservation.domain.reservation.service;

import com.google.gson.Gson;
import com.templlo.service.reservation.domain.reservation.client.UserClient;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationException;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.controller.model.request.CreateReservationReq;
import com.templlo.service.reservation.domain.reservation.controller.model.response.CancelReservationRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.CreateReservationRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.RejectReservationRes;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.repository.ReservationRepository;
import com.templlo.service.reservation.domain.reservation.service.model.produce.CancelReservationProduce;
import com.templlo.service.reservation.domain.reservation.service.model.produce.CreateReservationProduce;
import com.templlo.service.reservation.domain.reservation.service.model.produce.ReservationOpenType;
import com.templlo.service.reservation.global.common.exception.BaseException;
import com.templlo.service.reservation.global.common.response.BasicStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.templlo.service.reservation.domain.reservation.service.model.produce.ProducerTopic.TOPIC_CANCEL_RESERVATION;
import static com.templlo.service.reservation.domain.reservation.service.model.produce.ProducerTopic.TOPIC_CREATE_RESERVATION;

// TODO : Transactional outbox 패턴 적용하고 save() 사용 안함

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserClient userClient;
    private final Gson gson; // TODO : KafkaTemplate 새로 만들어서 처리하기

    // TODO : 함수의 책임, 트랜잭션에 대해 생각
    public CreateReservationRes createReservation(CreateReservationReq requestDto, String userId) {
        // TODO : 사용자 id 비교 검증

        // save
        Reservation reservation = requestDto.toEntity();
        Reservation savedReservation = reservationRepository.save(reservation);

        // 예약 신청 이벤트 발행
        produceReservationCreatedMessage(savedReservation, 0, ReservationOpenType.ADDITIONAL_OPEN);

        return CreateReservationRes.from(savedReservation);
    }

    public CancelReservationRes cancelReservation(UUID reservationId, String userId) {
        Reservation reservation = getReservation(reservationId);
        checkUser(userId, reservation);

        reservation.updateStatusProcessingCancel();
        Reservation savedReservation = reservationRepository.save(reservation);

        produceReservationCancelMessage(reservation, 0, ReservationOpenType.ADDITIONAL_OPEN);

        return CancelReservationRes.from(savedReservation);
    }

    public RejectReservationRes rejectReservation(UUID reservationId, String userId) {
        Reservation reservation = getReservation(reservationId);
        checkUser(userId, reservation);

        reservation.updateStatusProcessingReject();
        Reservation savedReservation = reservationRepository.save(reservation);

        produceReservationCancelMessage(reservation, 0, ReservationOpenType.ADDITIONAL_OPEN);

        return RejectReservationRes.from(savedReservation);
    }




    private void produceReservationCreatedMessage(Reservation savedReservation, int amount, ReservationOpenType openType) {
        CreateReservationProduce createReservationMessage = CreateReservationProduce.from(savedReservation, amount, openType);
        kafkaTemplate.send(TOPIC_CREATE_RESERVATION, null, gson.toJson(createReservationMessage));
    }

    private void produceReservationCancelMessage(Reservation savedReservation, int amount, ReservationOpenType openType) {
        CancelReservationProduce message = CancelReservationProduce.from(savedReservation, amount, openType);
        kafkaTemplate.send(TOPIC_CANCEL_RESERVATION, null, gson.toJson(message));
    }


    private void checkUser(String userId, Reservation reservation) {
        UUID userUUID = userClient.getUser(userId).data().id();

        if(!reservation.getUserId().equals(userUUID)) {
            throw new BaseException(BasicStatusCode.FORBIDDEN);
        }
    }

    private Reservation getReservation(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationStatusCode.RESERVATION_NOT_FOUND));
    }
}
