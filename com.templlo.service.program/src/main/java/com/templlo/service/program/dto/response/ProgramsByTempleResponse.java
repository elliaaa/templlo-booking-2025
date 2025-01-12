package com.templlo.service.program.dto.response;

import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramStatus;
import com.templlo.service.program.entity.ProgramType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ProgramsByTempleResponse(
        ProgramType type,
        UUID programId,
        Integer programFee,
        ProgramDetail detail
) {

    public static ProgramsByTempleResponse From(Program program, boolean detail) {

        if (!detail) {
            return ProgramsByTempleResponse.builder()
                    .type(program.getType())
                    .programId(program.getId())
                    .programFee(program.getProgramFee())
                    .detail(null)
                    .build();
        }

        return ProgramsByTempleResponse.builder()
                .type(program.getType())
                .programId(program.getId())
                .programFee(program.getProgramFee())
                .detail(
                        ProgramDetail.builder()
                                .title(program.getTitle())
                                .description(program.getDescription())
                                .programRating(program.getProgramRating())
                                .dates(
                                        // temple stay 해당 프로그램 date 정보들 다 가져옴
                                        program.getType() == ProgramType.TEMPLE_STAY
                                        ?
                                        program.getTempleStayDailyInfos().stream()
                                                .map(dailyInfo ->
                                                        new ProgramDateInfo(dailyInfo.getId(),dailyInfo.getProgramDate(), dailyInfo.getStatus())).toList()
                                        // blind date
                                        : List.of(new ProgramDateInfo(program.getBlindDateInfo().getId(), program.getBlindDateInfo().getProgramDate(), program.getBlindDateInfo().getStatus()))
                        )
                        .build()
                )
                .build();
    }

    @Builder
    public static record ProgramDetail(
            String title,
            String description,
            double programRating,
            List<ProgramDateInfo> dates
    ) {
    }
    @Builder
    public static record ProgramDateInfo(
            UUID programScheduleId,
            LocalDate date,
            ProgramStatus status
    ) {
    }
}
