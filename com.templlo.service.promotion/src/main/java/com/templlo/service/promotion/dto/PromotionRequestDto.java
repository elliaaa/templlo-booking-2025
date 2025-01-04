package com.templlo.service.promotion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PromotionRequestDto {
    private String name;
    private String type; // HOTDEAL, MEMBERSHIP 등
    private LocalDate startDate;
    private LocalDate endDate;
    private String couponType;
    private Integer totalCoupons;
}
