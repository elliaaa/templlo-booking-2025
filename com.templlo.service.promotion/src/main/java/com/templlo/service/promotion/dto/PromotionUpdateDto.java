package com.templlo.service.promotion.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PromotionUpdateDto(
	@NotBlank(message = "프로모션 이름은 필수입니다.")
	String name, // 프로모션 이름

	@NotNull(message = "시작일은 필수입니다.")
	LocalDate startDate, // 시작일

	@NotNull(message = "종료일은 필수입니다.")
	LocalDate endDate, // 종료일

	@NotNull(message = "남성 쿠폰 수량은 필수입니다.")
	Integer maleCoupons, // 남성 쿠폰 수량

	@NotNull(message = "여성 쿠폰 수량은 필수입니다.")
	Integer femaleCoupons, // 여성 쿠폰 수량

	@NotNull(message = "전체 쿠폰 수량은 필수입니다.")
	Integer totalCoupons, // 전체 쿠폰 수량

	@NotBlank(message = "쿠폰 유형은 필수입니다.")
	String couponType, // 쿠폰 유형 (예: DISCOUNT, ENTRY 등)

	String status // 프로모션 상태
) {
}
