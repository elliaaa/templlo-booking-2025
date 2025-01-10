package com.templlo.service.coupon.dto;

import java.util.UUID;

public record CouponIssuedResponseEvent(
	UUID userId,
	String status,
	UUID couponId
) {
}
