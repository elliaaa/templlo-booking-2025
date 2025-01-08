package com.templlo.service.coupon.dto;

import java.util.UUID;

public record CouponTransferRequestDto(
	UUID toUserId
) {
}
