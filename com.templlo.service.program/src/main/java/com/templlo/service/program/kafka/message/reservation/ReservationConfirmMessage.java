package com.templlo.service.program.kafka.message.reservation;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ReservationConfirmMessage(
        UUID reservationId,
        ReservationStatus status
) {
    public static ReservationConfirmMessage from(UUID reservationId, ReservationStatus status) throws Exception {

        return new ReservationConfirmMessage(reservationId, status);
    }
}
