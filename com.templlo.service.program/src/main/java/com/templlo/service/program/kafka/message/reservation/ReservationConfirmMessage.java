package com.templlo.service.program.kafka.message.reservation;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ReservationConfirmMessage(
        UUID reservationId,
        ReservationStatus status
) {
    public static ReservationConfirmMessage SuccessMessageFrom(UUID reservationId) {

        return new ReservationConfirmMessage(reservationId, ReservationStatus.SUCCESS);
    }

    public static ReservationConfirmMessage FailureMessageFrom(UUID reservationId) {

        return new ReservationConfirmMessage(reservationId, ReservationStatus.FAILURE);
    }
}
