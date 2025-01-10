package com.templlo.service.reservation.domain.reservation.service.model.produce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.domain.ReservationGenderType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Builder
public record CreateReservationProduce(
        UUID userId,
        UUID reservationId,
        UUID programId,

        String programDate,

        ReservationGenderType gender,
        Integer amount,
        CouponUsedType couponUsedType,
        UUID couponId
) {

    public static CreateReservationProduce from(Reservation reservation, int amount) {
        String programDateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(reservation.getProgramDate());
        return CreateReservationProduce.builder()
                .userId(reservation.getUserId())
                .reservationId(reservation.getReservationId())
                .programId(reservation.getProgramId())
                .programDate(programDateStr)
                .gender(reservation.getGender())
                .amount(amount)
                .couponUsedType(CouponUsedType.valueOfIsUsed(reservation.isCouponUsed()))
                .couponId(reservation.getCouponId())
                .build();
    }
}
