package com.templlo.service.program.dto.response;

import com.templlo.service.program.entity.*;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record TempleStayProgramResponse(

        UUID programId,
        UUID templeId,
        String title,
        String description,
        Integer programFee,
        LocalDate programDate,
        List<String> programDays,
        ProgramType type,
        ProgramStatus status,
        LocalTime programStartAt,
        Integer programCapacity,
        Integer availableCapacity,
        LocalDate reservationStartDate,
        LocalDate reservationEndDate

) implements DetailProgramResponse {

    public static TempleStayProgramResponse from(Program program, TempleStayDailyInfo templeStayDailyInfo) {
        return TempleStayProgramResponse.builder()
                .programId(program.getId())
                .templeId(program.getTempleId())
                .title(program.getTitle())
                .description(program.getDescription())
                .programFee(program.getProgramFee())
                .programDate(templeStayDailyInfo.getProgramDate())
                .programDays(program.convertProgramDaysToList(program.getProgramDays()))
                .type(program.getType())
                .status(templeStayDailyInfo.getStatus())
                .programStartAt(program.getProgramStartAt())
                .programCapacity(program.getProgramCapacity())
                .availableCapacity(templeStayDailyInfo.getAvailableCapacity())
                .reservationStartDate(program.getReservationStartDate())
                .reservationEndDate(program.getReservationEndDate())
                .build();
    }
}
