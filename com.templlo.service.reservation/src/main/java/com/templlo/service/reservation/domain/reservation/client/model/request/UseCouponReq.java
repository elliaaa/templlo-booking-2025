package com.templlo.service.reservation.domain.reservation.client.model.request;

import com.templlo.service.reservation.domain.reservation.client.model.response.DetailProgramRes;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record UseCouponReq(
        UUID programId,
        LocalDate programDate
) {
    public static UseCouponReq toDto(DetailProgramRes detailProgramRes, LocalDate programDate) {
        return UseCouponReq.builder()
                .programId(detailProgramRes.programId())
                .programDate(programDate)
                .build();
    }
}
