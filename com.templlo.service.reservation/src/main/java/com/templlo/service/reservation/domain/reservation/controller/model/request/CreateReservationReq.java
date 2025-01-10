package com.templlo.service.reservation.domain.reservation.controller.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.templlo.service.reservation.domain.reservation.domain.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record CreateReservationReq(
        @NotNull
        UUID userId,

        @NotNull
        UUID programId,

        // TODO : 날짜 형식 검사?
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @NotNull
        LocalDate programDate,

        @NotNull
        CouponUsedType couponUsedType,

        @NotBlank
        String name,

        @Pattern(regexp = "/^0\\d{1,2}-\\d{3,4}-\\d{4}$/", message = "전화번호 형식에 맞지 않음")
        @NotBlank
        String phoneNumber,

        @NotNull
        ReservationGenderType gender,

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
                .programDate(this.programDate)
                .userId(this.userId)
                .status(ReservationStatus.PROCESSING)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .gender(this.gender)
                .paymentStatus(this.paymentStatus)
                .paymentType(this.paymentType)
                .isCouponUsed(this.couponUsedType.isUsed())
                .couponId(this.couponId)
                .build();
    }
}
