package com.templlo.service.reservation.domain.reservation.service;

import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationException;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.controller.model.response.ReservationDetailRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.ReservationListRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.ReservationListWrapperRes;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.repository.ReservationRepository;
import com.templlo.service.reservation.global.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationQueryService {
    public final ReservationRepository reservationRepository;

    public ReservationDetailRes getReservationById(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationStatusCode.RESERVATION_NOT_FOUND));
        return ReservationDetailRes.from(reservation);
    }

    public ReservationListWrapperRes getReservationsByUser(UUID userId, Pageable pageable) {
        PagedModel<ReservationListRes> dtos = reservationRepository.findAllByUserIdOfPagedModel(userId, pageable);
        PageResponse<ReservationListRes> pageResponseDtos = PageResponse.of(dtos);
        return ReservationListWrapperRes.from(userId, pageResponseDtos);
    }
}
