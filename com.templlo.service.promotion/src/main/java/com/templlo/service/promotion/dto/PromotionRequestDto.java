package com.templlo.service.promotion.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PromotionRequestDto(
	@NotBlank(message = "프로모션 이름은 필수입니다.")
	String name,

	@NotBlank(message = "프로모션 유형은 필수입니다.")
	String type, // HOTDEAL, MEMBERSHIP 등

	@NotNull(message = "시작일은 필수입니다.")
	LocalDate startDate,

	@NotNull(message = "종료일은 필수입니다.")
	LocalDate endDate,

	@NotBlank(message = "쿠폰 유형은 필수입니다.")
	String couponType, // 할인, 입장권 등

	Integer maleCoupon,

	Integer femaleCoupon,

	@NotNull(message = "전체 쿠폰 수량은 필수입니다.")
	Integer totalCoupon,

	String status, // ACTIVE, INACTIVE 등

	@DecimalMin(value = "0.0", inclusive = false, message = "할인 값은 0보다 커야 합니다.")
	BigDecimal value, // 할인 금액 또는 비율

	String discountType // PERCENTAGE, AMOUNT
) {
	public Integer maleCoupon() {
		return maleCoupon == null ? 0 : maleCoupon; // null인 경우 기본값 0 반환
	}

	public Integer femaleCoupon() {
		return femaleCoupon == null ? 0 : femaleCoupon; // null인 경우 기본값 0 반환
	}

	public String discountType() {
		return discountType == null || discountType.isBlank() ? "AMOUNT" : discountType; // 기본값 AMOUNT 반환
	}
}
