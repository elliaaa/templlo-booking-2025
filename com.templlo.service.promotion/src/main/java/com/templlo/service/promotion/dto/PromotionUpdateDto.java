package com.templlo.service.promotion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PromotionUpdateDto {
    private String name; // 프로모션 이름
    private LocalDate startDate; // 시작일
    private LocalDate endDate; // 종료일
    private Integer maleCoupons; // 남성 쿠폰 수량
    private Integer femaleCoupons; // 여성 쿠폰 수량
    private Integer totalCoupons; // 전체 쿠폰 수량
    private String couponType; // 쿠폰 유형 (예: DISCOUNT, ENTRY 등)
}
