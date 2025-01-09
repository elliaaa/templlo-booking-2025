package com.templlo.service.coupon.dto;

import jakarta.validation.constraints.NotNull;

public record TicketApplyRequestDto(
	@NotNull(message = "사용자 ID는 필수입니다.")
	String userId,

	@NotNull(message = "프로그램 ID는 필수입니다.")
	String programId
) {
	public TicketApplyRequestDto() {
		this(null, null);
	}
}
