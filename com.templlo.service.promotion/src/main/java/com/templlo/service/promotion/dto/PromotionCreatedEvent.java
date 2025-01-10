package com.templlo.service.promotion.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PromotionCreatedEvent(
	UUID promotionId,
	String name,
	LocalDate startDate,
	LocalDate endDate,
	String couponType,
	int totalCoupons
) {
}