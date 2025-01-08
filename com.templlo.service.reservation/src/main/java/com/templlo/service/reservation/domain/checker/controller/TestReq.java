package com.templlo.service.reservation.domain.checker.controller;

import com.templlo.service.reservation.domain.reservation.domain.ReservationStatus;

public record TestReq(
        String name,
        String status,
        ReservationStatus reservationStatus
) {
}
