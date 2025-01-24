package com.templlo.service.reservation.domain.reservation.service.model.consume;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CancelReservationResultConsume(
        UUID reservationId,
        CancelReservationResultStatus status
) {
}
