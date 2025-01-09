package com.templlo.service.coupon.dto;

public record CouponStatusResponseDto(
	String promotionId,
	int totalCoupons,
	int issuedCoupons,
	int remainingCoupons
) {
}
