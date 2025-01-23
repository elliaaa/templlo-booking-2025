package com.templlo.service.reservation.domain.reservation.service.model.produce;

import com.templlo.service.reservation.domain.reservation.controller.model.request.CouponUsedType;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.domain.ReservationGenderType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CancelReservationProduce(
        UUID reservationId,
        UUID programId,
        String programDate,
        ReservationOpenType openType,
        ReservationGenderType gender,
        Integer amount
) {

    public static CancelReservationProduce from(Reservation reservation, int amount, ReservationOpenType openType) {
        return CancelReservationProduce.builder()
                .reservationId(reservation.getReservationId())
                .programId(reservation.getProgramId())
                .programDate(reservation.getProgramDateFormatted())
                .openType(openType)
                .gender(reservation.getGender())
                .amount(amount)
                .build();
    }
}
