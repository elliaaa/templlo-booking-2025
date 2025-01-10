package com.templlo.service.reservation.domain.reservation.controller.model.response;

import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ReservationListRes(
        UUID reservationId,
        UUID programId,
        String programDate,
        UUID userId,
        ReservationStatus status,
        String name,
        String phoneNumber,
        ReservationGenderType gender,
        CouponUsedType couponUsedType,
        LocalDateTime createdAt
) {

    public static ReservationListRes from(Reservation reservation) {
        CouponUsedType couponUsedType = CouponUsedType.valueOfIsUsed(reservation.isCouponUsed());
        return ReservationListRes.builder()
                .reservationId(reservation.getReservationId())
                .programId(reservation.getProgramId())
                .programDate(reservation.getProgramDateFormatted())
                .userId(reservation.getUserId())
                .status(reservation.getStatus())
                .name(reservation.getName())
                .phoneNumber(reservation.getPhoneNumber())
                .gender(reservation.getGender())
                .couponUsedType(couponUsedType)
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}
