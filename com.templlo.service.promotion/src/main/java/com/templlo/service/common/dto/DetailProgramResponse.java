package com.templlo.service.common.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record DetailProgramResponse(
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
