package com.templlo.service.promotion.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class PromotionDetailResponseDto {
    private UUID promotionId;           // 프로모션 ID
    private String name;                // 프로모션 이름
    private String type;                // 프로모션 유형
    private LocalDate startDate;        // 시작일
    private LocalDate endDate;          // 종료일
    private Integer totalCoupon;        // 전체 쿠폰 수량
    private Integer issuedCoupon;       // 발급된 쿠폰 수량
    private Integer remainingCoupon;    // 남은 쿠폰 수량
    private Integer maleCoupons;        // 남성 쿠폰 수량
    private Integer femaleCoupons;      // 여성 쿠폰 수량
    private String status;              // 프로모션 상태
}
