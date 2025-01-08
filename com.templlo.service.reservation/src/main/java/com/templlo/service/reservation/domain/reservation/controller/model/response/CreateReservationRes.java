package com.templlo.service.reservation.domain.reservation.controller.model.response;

import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.PaymentStatus;
import com.templlo.service.reservation.domain.reservation.domain.PaymentType;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.domain.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateReservationRes(
        UUID reservationId,
        UUID programId,
        UUID programScheduleId,
        UUID userId,
        ReservationStatus status,
        String name,
        String phoneNumber,
        CouponUsedType couponUsedType,
        UUID couponId,
        PaymentStatus paymentStatus,
        PaymentType paymentType,
        LocalDateTime createdAt,
        String createdBy
) {

    public static CreateReservationRes from(Reservation reservation) {
        CouponUsedType couponUsedType = CouponUsedType.valueOfIsUsed(reservation.isCouponUsed());

        return CreateReservationRes.builder()
                .reservationId(reservation.getReservationId())
                .programId(reservation.getProgramId())
                .programScheduleId(reservation.getProgramScheduleId())
                .userId(reservation.getUserId())
                .status(reservation.getStatus())
                .name(reservation.getName())
                .phoneNumber(reservation.getPhoneNumber())
                .couponUsedType(couponUsedType)
                .couponId(reservation.getCouponId())
                .paymentStatus(reservation.getPaymentStatus())
                .paymentType(reservation.getPaymentType())
                .createdAt(reservation.getCreatedAt())
                .createdBy(reservation.getCreatedBy())
                .build();
    }
}
