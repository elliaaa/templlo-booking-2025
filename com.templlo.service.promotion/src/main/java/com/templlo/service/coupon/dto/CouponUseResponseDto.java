package com.templlo.service.coupon.dto;

import java.math.BigDecimal;

public record CouponUseResponseDto(String status, String message, BigDecimal finalPrice) {
	public CouponUseResponseDto(String status, String message) {
		this(status, message, null); // 기본 생성자: 금액 없는 경우
	}
}

