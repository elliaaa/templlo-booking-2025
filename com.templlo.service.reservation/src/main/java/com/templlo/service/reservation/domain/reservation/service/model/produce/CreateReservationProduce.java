package com.templlo.service.reservation.domain.reservation.service.model.produce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.domain.ReservationGenderType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.templlo.service.reservation.global.GlobalConst.PROGRAM_DATE_FORMAT;

@Builder
public record CreateReservationProduce(
        UUID userId,
        UUID reservationId,
        UUID programId,
        String programDate,
        ReservationOpenType openType,
        ReservationGenderType gender,
        Integer amount,
        CouponUsedType couponUsedType,
        UUID couponId
) {

    public static CreateReservationProduce from(Reservation reservation, int amount, ReservationOpenType openType) {
        return CreateReservationProduce.builder()
                .userId(reservation.getUserId())
                .reservationId(reservation.getReservationId())
                .programId(reservation.getProgramId())
                .programDate(reservation.getProgramDateFormatted())
                .openType(openType)
                .gender(reservation.getGender())
                .amount(amount)
                .couponUsedType(CouponUsedType.valueOfIsUsed(reservation.isCouponUsed()))
                .couponId(reservation.getCouponId())
                .build();
    }
}
