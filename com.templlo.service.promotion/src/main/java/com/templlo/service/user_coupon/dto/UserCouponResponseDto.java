package com.templlo.service.user_coupon.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.templlo.service.coupon.dto.CouponResponseDto;

public record UserCouponResponseDto(
	UUID userCouponId,
	UUID userId,
	String userLoginId,
	String status,
	LocalDateTime issuedAt,
	LocalDateTime usedAt,
	CouponResponseDto coupon
) {
}
