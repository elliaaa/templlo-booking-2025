package com.templlo.service.reservation.domain.reservation.controller;

import com.templlo.service.reservation.domain.reservation.controller.exception.ReservationStatusCode;
import com.templlo.service.reservation.domain.reservation.controller.model.request.CreateReservationReq;
import com.templlo.service.reservation.domain.reservation.controller.model.response.CancelReservationRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.CreateReservationRes;
import com.templlo.service.reservation.domain.reservation.controller.model.response.RejectReservationRes;
import com.templlo.service.reservation.domain.reservation.service.ReservationCommandService;
import com.templlo.service.reservation.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationCommandController {
    public final ReservationCommandService reservationCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateReservationRes>> createReservation(
            @RequestBody CreateReservationReq requestDto,
            @AuthenticationPrincipal String userId) {
        CreateReservationRes responseDto = reservationCommandService.createReservation(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(ReservationStatusCode.CREATE_RESERVATION_SUCCESS, responseDto));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<CancelReservationRes>> cancelReservation(
            @PathVariable UUID reservationId,
            @AuthenticationPrincipal String userId) {
        CancelReservationRes responseDto = reservationCommandService.cancelReservation(reservationId, userId);
        return ResponseEntity.ok().body(ApiResponse.of(ReservationStatusCode.CANCEL_RESERVATION_SUCCESS, responseDto));
    }

    @PatchMapping("/{reservationId}/reject")
    public ResponseEntity<ApiResponse<RejectReservationRes>> rejectReservation(
            @PathVariable UUID reservationId,
            @AuthenticationPrincipal String userId) {
        RejectReservationRes responseDto = reservationCommandService.rejectReservation(reservationId, userId);
        return ResponseEntity.ok().body(ApiResponse.of(ReservationStatusCode.REJECT_RESERVATION_SUCCESS, responseDto));
    }
}
