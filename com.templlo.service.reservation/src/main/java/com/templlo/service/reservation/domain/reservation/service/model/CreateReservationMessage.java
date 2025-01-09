package com.templlo.service.reservation.domain.reservation.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.domain.ReservationGenderType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record CreateReservationMessage(
        UUID userId,
        UUID reservationId,
        UUID programId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate programDate,

        ReservationGenderType gender,
        Integer amount,
        CouponUsedType couponUsedType,
        UUID couponId
) {

    public static CreateReservationMessage from(Reservation reservation, int amount) {
        return CreateReservationMessage.builder()
                .userId(reservation.getUserId())
                .reservationId(reservation.getReservationId())
                .programId(reservation.getProgramId())
                .programDate(reservation.getProgramDate())
                .gender(reservation.getGender())
                .amount(amount)
                .couponUsedType(CouponUsedType.valueOfIsUsed(reservation.isCouponUsed()))
                .couponId(reservation.getCouponId())
                .build();
    }
}
