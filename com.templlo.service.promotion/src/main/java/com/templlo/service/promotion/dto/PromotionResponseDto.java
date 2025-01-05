package com.templlo.service.promotion.dto;

import java.util.UUID;

public record PromotionResponseDto(
	UUID promotionId,
	String status, // SUCCESS or FAILURE
	String message // 응답 메시지
) {
}
