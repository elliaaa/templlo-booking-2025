package com.templlo.service.coupon.dto;

public record TicketApplyResponseDto(
	String status,
	int queuePosition,
	String message
) {
}
