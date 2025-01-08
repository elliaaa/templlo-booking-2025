package com.templlo.service.reservation.domain.reservation.controller.model.request;

import com.templlo.service.reservation.domain.reservation.domain.PaymentStatus;
import com.templlo.service.reservation.domain.reservation.domain.PaymentType;
import com.templlo.service.reservation.domain.reservation.domain.Reservation;
import com.templlo.service.reservation.domain.reservation.domain.ReservationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateReservationReq(
        @NotNull
        UUID userId,

        @NotNull
        UUID programId,

        @NotNull
        UUID programScheduleId,

        @NotNull
        CouponUsedType couponUsedType,

        @NotBlank
        String name,

        @Pattern(regexp = "/^0\\d{1,2}-\\d{3,4}-\\d{4}$/", message = "전화번호 형식에 맞지 않음")
        @NotBlank
        String phoneNumber,

        UUID couponId,

        @NotNull
        PaymentType paymentType,

        PaymentStatus paymentStatus
) {

    public CreateReservationReq {
        if (paymentStatus == null) { paymentStatus = PaymentStatus.PENDING; }
    }

    public Reservation toEntity() {
        return Reservation.builder()
                .programId(this.programId)
                .programScheduleId(this.programScheduleId)
                .userId(this.userId)
                .status(ReservationStatus.PROCESSING)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .paymentStatus(this.paymentStatus)
                .paymentType(this.paymentType)
                .isCouponUsed(this.couponUsedType.isUsed())
                .couponId(this.couponId)
                .build();
    }
}
