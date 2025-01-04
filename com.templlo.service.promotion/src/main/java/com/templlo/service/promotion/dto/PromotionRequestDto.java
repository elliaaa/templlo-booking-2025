package com.templlo.service.promotion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PromotionRequestDto {

    @NotBlank(message = "프로모션 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "프로모션 유형은 필수입니다.")
    private String type; // HOTDEAL, MEMBERSHIP 등

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDate endDate;

    @NotBlank(message = "쿠폰 유형은 필수입니다.")
    private String couponType; // 할인, 입장권 등

    private Integer maleCoupon;
    private Integer femaleCoupon;

    @NotNull(message = "전체 쿠폰 수량은 필수입니다.")
    private Integer totalCoupon;

    private String status; // ACTIVE, INACTIVE 등
}
