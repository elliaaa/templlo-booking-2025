package com.templlo.service.coupon.dto;

import java.util.UUID;

public record CouponIssueRequestDto(
	UUID couponId,
	UUID userId,
	UUID programId,
	String gender,
	String details
) {
}
