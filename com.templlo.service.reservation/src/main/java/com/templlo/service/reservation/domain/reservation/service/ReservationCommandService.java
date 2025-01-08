package com.templlo.service.reservation.domain.reservation.service;

import com.templlo.service.reservation.domain.reservation.controller.model.request.CreateReservationReq;
import com.templlo.service.reservation.domain.reservation.controller.model.response.CreateReservationRes;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;

    public CreateReservationRes createReservation(CreateReservationReq requestDto, String userId) {
        Reservation reservation = requestDto.toEntity();
        Reservation savedReservation = reservationRepository.save(reservation);
        return CreateReservationRes.from(savedReservation);
    }
}
