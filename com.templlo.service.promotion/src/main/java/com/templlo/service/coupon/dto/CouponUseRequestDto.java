package com.templlo.service.coupon.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CouponUseRequestDto(
	UUID programId,
	LocalDate programDate
) {
}
