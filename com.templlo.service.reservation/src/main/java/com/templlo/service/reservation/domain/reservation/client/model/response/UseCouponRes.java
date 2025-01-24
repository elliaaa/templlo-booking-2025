package com.templlo.service.reservation.domain.reservation.client.model.response;

public record UseCouponRes(
        String status,
        String message,
        Integer finalPrice
) {
}
