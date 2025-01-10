package com.templlo.service.program.dto.response;

import com.templlo.service.program.entity.*;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record TempleStayScheduleResponse(

        UUID programId,
        Integer programFee,
        LocalDate programDate,
        ProgramStatus status,
        LocalTime programStartAt,
        Integer programCapacity,
        Integer availableCapacity


) implements ProgramScheduleResponse {

    public static TempleStayScheduleResponse from(Program program, TempleStayDailyInfo templeStayDailyInfo) {
        return TempleStayScheduleResponse.builder()
                .programId(program.getId())
                .programFee(program.getProgramFee())
                .programDate(templeStayDailyInfo.getProgramDate())
                .status(templeStayDailyInfo.getStatus())
                .programStartAt(program.getProgramStartAt())
                .programCapacity(program.getProgramCapacity())
                .availableCapacity(templeStayDailyInfo.getAvailableCapacity())
                .build();
    }
}
