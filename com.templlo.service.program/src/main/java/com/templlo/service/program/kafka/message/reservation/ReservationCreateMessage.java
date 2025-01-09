package com.templlo.service.program.kafka.message.reservation;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationCreateMessage(
        UUID userId,
        UUID reservationId,
        UUID programId,
        LocalDate programDate,
        Gender gender,
        CouponUsedType couponUsedType,
        UUID couponId
) {
}
