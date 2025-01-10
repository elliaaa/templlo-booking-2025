package com.templlo.service.coupon.dto;

import java.util.UUID;

import com.templlo.service.promotion.dto.PromotionResponseDto;

public record CouponResponseDto(
	UUID couponId,
	String createdBy,
	String updatedBy,
	PromotionResponseDto promotion
) {
}
