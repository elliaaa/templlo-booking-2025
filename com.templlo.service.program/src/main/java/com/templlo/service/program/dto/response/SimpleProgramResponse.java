package com.templlo.service.program.dto.response;

import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record SimpleProgramResponse(
        UUID programId,
        UUID templeId,
        String title,
        String description,
        ProgramType type,
        Integer programFee,
        LocalTime programStartAt,
        LocalDate reservationStartDate,
        LocalDate reservationEndDate,
        List<String> programDays,
        Integer programCapacity
) {

   public static SimpleProgramResponse from(Program program) {

       return SimpleProgramResponse.builder()
               .programId(program.getId())
               .templeId(program.getTempleId())
               .title(program.getTitle())
               .description(program.getDescription())
               .type(program.getType())
               .programFee(program.getProgramFee())
               .programStartAt(program.getProgramStartAt())
               .reservationStartDate(program.getReservationStartDate())
               .reservationEndDate(program.getReservationEndDate())
               .programDays(program.convertProgramDaysToList(program.getProgramDays()))
               .programCapacity(program.getProgramCapacity())
               .build();

   }
}
