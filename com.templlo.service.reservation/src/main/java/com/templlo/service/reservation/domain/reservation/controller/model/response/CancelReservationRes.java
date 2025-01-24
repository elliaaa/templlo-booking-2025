package com.templlo.service.reservation.domain.reservation.controller.model.response;

import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.*;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record CancelReservationRes(
        UUID reservationId,
        UUID programId,
        String programDate,
        UUID userId,
        ReservationStatus status,
        String name,
        String phoneNumber,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy,
        LocalDateTime deletedAt,
        String deletedBy,
        boolean isDeleted
) {
    public static CancelReservationRes from(Reservation reservation) {
        CouponUsedType couponUsedType = CouponUsedType.valueOfIsUsed(reservation.isCouponUsed());
        return CancelReservationRes.builder()
                .reservationId(reservation.getReservationId())
                .programId(reservation.getProgramId())
                .programDate(reservation.getProgramDateFormatted())
                .userId(reservation.getUserId())
                .status(reservation.getStatus())
                .name(reservation.getName())
                .phoneNumber(reservation.getPhoneNumber())
                .createdAt(reservation.getCreatedAt())
                .createdBy(reservation.getCreatedBy())
                .updatedAt(reservation.getUpdatedAt())
                .updatedBy(reservation.getUpdatedBy())
                .deletedAt(reservation.getDeletedAt())
                .deletedBy(reservation.getDeletedBy())
                .isDeleted(reservation.isDeleted())
                .build();
    }
}
