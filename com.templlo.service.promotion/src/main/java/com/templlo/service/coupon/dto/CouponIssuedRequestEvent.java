package com.templlo.service.coupon.dto;

import java.util.UUID;

/**
 * Kafka에서 쿠폰 발급 요청을 처리하기 위한 이벤트 DTO
 */
public record CouponIssuedRequestEvent(
	UUID promotionId,  // 프로모션 ID
	UUID userId,       // 사용자 ID
	String userLoginId, // 사용자 로그인 ID
	String gender,     // 성별 (MALE, FEMALE 또는 NULL)
	String details     // 기타 상세 정보
) {
}
