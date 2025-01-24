package com.templlo.service.reservation.domain.reservation.client.model.request;

import com.templlo.service.reservation.domain.reservation.client.model.response.DetailProgramResponse;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UseCouponReq(
        UUID programId,
        LocalDate programDate
) {
    public static UseCouponReq toDto(DetailProgramResponse detailProgramResponse, LocalDate programDate) {
        return UseCouponReq.builder()
                .programId(detailProgramResponse.programId())
                .programDate(programDate)
                .build();
    }
}
