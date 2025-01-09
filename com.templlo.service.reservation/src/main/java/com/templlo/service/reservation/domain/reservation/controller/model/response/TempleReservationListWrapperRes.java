package com.templlo.service.reservation.domain.reservation.controller.model.response;

import com.templlo.service.reservation.global.common.response.PageResponse;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record TempleReservationListWrapperRes(
        UUID templeId,
        PageResponse<ReservationListRes> reservations
) {
    public static TempleReservationListWrapperRes from(UUID templeId, PageResponse<ReservationListRes> reservations) {
        return TempleReservationListWrapperRes.builder()
                .templeId(templeId)
                .reservations(reservations)
                .build();
    }
}
