package com.templlo.service.coupon.dto;

import jakarta.validation.constraints.NotBlank;

public record CouponUpdateRequestDto(
	@NotBlank(message = "쿠폰 상태는 필수입니다.")
	String status // 예: "AVAILABLE", "ISSUED", "EXPIRED"
) {
}
