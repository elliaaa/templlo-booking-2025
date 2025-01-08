package com.templlo.service.coupon.dto;

public record CouponIssueRequestDto(
	String couponId,
	String userId,
	String programId,
	String gender,
	String details
) {
}
