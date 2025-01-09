package com.templlo.service.reservation.domain.reservation.controller.model.response;

import com.templlo.service.reservation.global.common.response.PageResponse;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UserReservationListWrapperRes(
        UUID userId,
        PageResponse<ReservationListRes> reservations
) {
    public static UserReservationListWrapperRes from(UUID userId, PageResponse<ReservationListRes> reservations) {
        return UserReservationListWrapperRes.builder()
                .userId(userId)
                .reservations(reservations)
                .build();
    }
}
