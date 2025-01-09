package com.templlo.service.coupon.dto;

public record CouponValidationResponseDto(
	boolean isValid,
	String message
) {
}
