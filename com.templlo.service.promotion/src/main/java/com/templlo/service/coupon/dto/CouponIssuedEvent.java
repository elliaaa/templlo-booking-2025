package com.templlo.service.coupon.dto;

import java.time.LocalDateTime;

public record CouponIssuedEvent(
	String couponId,
	String userId,
	String promotionId,
	String programId,
	String gender,
	String status,
	LocalDateTime issuedAt
) {
}
