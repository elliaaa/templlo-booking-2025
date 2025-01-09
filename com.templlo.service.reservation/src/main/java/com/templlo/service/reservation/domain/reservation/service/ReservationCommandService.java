package com.templlo.service.reservation.domain.reservation.service;

import com.google.gson.Gson;
import com.templlo.service.reservation.domain.reservation.controller.model.request.CreateReservationReq;
import com.templlo.service.reservation.domain.reservation.controller.model.response.CreateReservationRes;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.repository.ReservationRepository;
import com.templlo.service.reservation.domain.reservation.service.model.CreateReservationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.templlo.service.reservation.global.GlobalConst.TOPIC_CREATE_RESERVATION;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;
    public final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson; // TODO : KafkaTemplate 새로 만들어서 처리하기

    // TODO : 함수의 책임, 트랜잭션에 대해 생각
    public CreateReservationRes createReservation(CreateReservationReq requestDto, String userId) {
        // TODO : 사용자 id 비교 검증

        // TODO : 예약 받는 시간 중인지 check?

        // save
        Reservation reservation = requestDto.toEntity();
        Reservation savedReservation = reservationRepository.save(reservation);

        // 예약 신청 이벤트 발행
        CreateReservationMessage createReservationMessage = CreateReservationMessage.from(savedReservation, 1);

        kafkaTemplate.send(TOPIC_CREATE_RESERVATION, null, gson.toJson(createReservationMessage));

        return CreateReservationRes.from(savedReservation);
    }
}
