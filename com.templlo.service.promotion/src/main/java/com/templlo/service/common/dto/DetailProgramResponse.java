package com.templlo.service.common.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DetailProgramResponse(
	UUID programId,
	String type,
	BigDecimal programFee,
	LocalDate programDate
) {
}
