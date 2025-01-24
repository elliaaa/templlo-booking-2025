package com.templlo.service.reservation.domain.reservation.client.model.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record DetailProgramRes(
		UUID programId,
		UUID templeId,
		String title,
		String description,
		Double programRating,
		ProgramType type,
		Integer programFee,
		LocalTime programStartAt,
		LocalDate reservationStartDate,
		LocalDate reservationEndDate,
		List<String> programDays,
		Integer programCapacity
) {
}
