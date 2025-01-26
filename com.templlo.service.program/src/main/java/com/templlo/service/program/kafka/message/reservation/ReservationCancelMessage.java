package com.templlo.service.program.kafka.message.reservation;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationCancelMessage(
        UUID reservationId,
        UUID programId,
        LocalDate programDate,
        ReservationOpenType openType,
        Gender gender,
        Integer amount
) {
}
