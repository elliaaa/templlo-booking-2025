package com.templlo.service.kafka.dto;

import java.time.LocalDate;

public record ReservationCreatedEvent(
	String userId,
	String reservationId,
	String programId,
	LocalDate programDate,
	String openType,
	String gender,
	int amount,
	String couponUsedType,
	String couponId
) {
}
