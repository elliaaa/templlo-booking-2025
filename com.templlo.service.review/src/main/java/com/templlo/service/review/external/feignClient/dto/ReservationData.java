package com.templlo.service.review.external.feignClient.dto;

import java.util.UUID;

public record ReservationData(
	UUID reservationId,
	UUID programId,
	String programDate,
	UUID userId,
	ReservationStatus status,
	String name,
	String phoneNumber
) {
}
