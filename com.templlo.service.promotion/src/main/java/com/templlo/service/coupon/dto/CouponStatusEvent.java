package com.templlo.service.coupon.dto;

public record CouponStatusEvent(
	String promotionId,
	int totalCoupons,
	int issuedCoupons,
	int remainingCoupons
) {
}
