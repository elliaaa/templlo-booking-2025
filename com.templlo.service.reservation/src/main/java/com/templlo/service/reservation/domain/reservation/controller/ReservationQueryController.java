package com.templlo.service.reservation.domain.reservation.controller;

import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.controller.model.response.ReservationDetailRes;
import com.templlo.service.reservation.domain.reservation.service.ReservationQueryService;
import com.templlo.service.reservation.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationQueryController {
    private final ReservationQueryService reservationQueryService;

    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDetailRes>> getReservationDetail(
            @PathVariable(name = "reservationId") UUID reservationId
    ) {
        ReservationDetailRes responseDto = reservationQueryService.getReservationById(reservationId);
        return ResponseEntity.ok().body(
                ApiResponse.of(ReservationStatusCode.GET_RESERVATION_SUCCESS, responseDto));
    }
}
