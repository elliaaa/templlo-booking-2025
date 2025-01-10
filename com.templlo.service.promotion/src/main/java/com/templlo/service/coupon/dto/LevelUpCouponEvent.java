package com.templlo.service.coupon.dto;

import java.util.UUID;

public record LevelUpCouponEvent(
	UUID userId,          // 쿠폰을 받을 사용자 ID
	String couponId,      // 발급된 쿠폰 ID
	String message        // 관련 메시지 (예: "Level Up 쿠폰 발급 완료")
) {
}
