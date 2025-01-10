package com.templlo.service.reservation.domain.reservation.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.*;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateReservationRes(
        UUID reservationId,
        UUID programId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate programDate,

        UUID userId,
        ReservationStatus status,
        String name,
        String phoneNumber,
        ReservationGenderType gender,
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
                .programDate(reservation.getProgramDate())
                .userId(reservation.getUserId())
                .status(reservation.getStatus())
                .name(reservation.getName())
                .phoneNumber(reservation.getPhoneNumber())
                .gender(reservation.getGender())
                .couponUsedType(couponUsedType)
                .couponId(reservation.getCouponId())
                .paymentStatus(reservation.getPaymentStatus())
                .paymentType(reservation.getPaymentType())
                .createdAt(reservation.getCreatedAt())
                .createdBy(reservation.getCreatedBy())
                .build();
    }
}
