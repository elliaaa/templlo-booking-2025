package com.templlo.service.promotion.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PromotionResponseDto {
    private UUID promotionId;
    private String status; // SUCCESS or FAILURE
    private String message; // 응답 메시지
}
