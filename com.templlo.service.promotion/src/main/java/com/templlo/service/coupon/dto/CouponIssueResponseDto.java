package com.templlo.service.coupon.dto;

import java.util.UUID;

public record CouponIssueResponseDto(
	String status,
	UUID couponId,
	String message
) {
}
