package com.templlo.service.common.dto;

import java.time.LocalDate;
import java.util.UUID;

public record DetailProgramResponse(
	UUID programId,
	String type,
	int programFee,
	LocalDate programDate
) {
}
