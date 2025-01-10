package com.templlo.service.reservation.domain.reservation.service;

import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationException;
import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.controller.model.response.ReservationDetailRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.ReservationListRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.TempleReservationListWrapperRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.UserReservationListWrapperRes;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.repository.ReservationRepository;
import com.templlo.service.reservation.global.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationQueryService {
    private final ReservationRepository reservationRepository;
//    public final TempleClient templeClient;

    public ReservationDetailRes getReservationById(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationStatusCode.RESERVATION_NOT_FOUND));
        return ReservationDetailRes.from(reservation);
    }

    public UserReservationListWrapperRes getReservationsByUser(UUID userId, Pageable pageable) {
        PagedModel<ReservationListRes> dtos = reservationRepository.findAllByUserIdOfPagedModel(userId, pageable);
        PageResponse<ReservationListRes> pageResponseDtos = PageResponse.of(dtos);
        return UserReservationListWrapperRes.from(userId, pageResponseDtos);
    }

    public TempleReservationListWrapperRes getReservationsByTemple(UUID templeId, Pageable pageable, UUID tempProgramId1, UUID tempProgramId2) {
        // TODO : templeId 로 feign client 요청 - temple 에 속한 program id 목록 받아오기
        List<UUID> tempProgramIds = List.of(tempProgramId1, tempProgramId2);

        PagedModel<ReservationListRes> dtos = reservationRepository.findAllByByProgramIdOfPagedModel(tempProgramIds, pageable);
        PageResponse<ReservationListRes> pageResponseDtos = PageResponse.of(dtos);
        return TempleReservationListWrapperRes.from(templeId, pageResponseDtos);

    }
}
