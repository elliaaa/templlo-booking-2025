package com.templlo.service.coupon.dto;

public record CouponUpdateResponseDto(
	String status,  // 성공 여부
	String couponId, // 수정된 쿠폰 ID
	String message   // 결과 메시지
) {
}
