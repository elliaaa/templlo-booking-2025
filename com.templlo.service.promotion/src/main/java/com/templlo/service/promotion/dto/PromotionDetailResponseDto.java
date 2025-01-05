package com.templlo.service.promotion.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PromotionDetailResponseDto(
	UUID promotionId,           // 프로모션 ID
	String name,                // 프로모션 이름
	String type,                // 프로모션 유형
	LocalDate startDate,        // 시작일
	LocalDate endDate,          // 종료일
	Integer totalCoupon,        // 전체 쿠폰 수량
	Integer issuedCoupon,       // 발급된 쿠폰 수량
	Integer remainingCoupon,    // 남은 쿠폰 수량
	Integer maleCoupons,        // 남성 쿠폰 수량
	Integer femaleCoupons,      // 여성 쿠폰 수량
	String status               // 프로모션 상태
) {
}
