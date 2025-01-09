package com.templlo.service.program.dto.response;

import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramGenderStatus;
import com.templlo.service.program.entity.ProgramStatus;
import com.templlo.service.program.entity.ProgramType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record BlindDateProgramResponse(

        UUID programId,
        UUID templeId,
        String title,
        String description,
        LocalDate programDate,
        ProgramType type,
        ProgramStatus status,
        ProgramGenderStatus genderStatus,
        LocalTime programStartAt,
        Integer programCapacity,
        Integer availableMaleCapacity,
        Integer availableFemaleCapacity,
        LocalDate reservationStartDate,
        LocalDate reservationEndDate,
        LocalDate additionalReservationStartDate,
        LocalDate additionalReservationEndDate

) implements DetailProgramResponse {

    public static BlindDateProgramResponse from(Program program) {
        return BlindDateProgramResponse.builder()
                .programId(program.getId())
                .templeId(program.getTempleId())
                .title(program.getTitle())
                .description(program.getDescription())
                .programDate(program.getBlindDateInfo().getProgramDate())
                .type(program.getType())
                .status(program.getBlindDateInfo().getStatus())
                .genderStatus(program.getBlindDateInfo().getGenderStatus())
                .programStartAt(program.getProgramStartAt())
                .programCapacity(program.getProgramCapacity())
                .availableMaleCapacity(program.getBlindDateInfo().getAvailableMaleCapacity())
                .availableFemaleCapacity(program.getBlindDateInfo().getAvailableFemaleCapacity())
                .reservationStartDate(program.getReservationStartDate())
                .reservationEndDate(program.getReservationEndDate())
                .additionalReservationStartDate(program.getBlindDateInfo().getAdditionalReservationStartDate())
                .additionalReservationEndDate(program.getBlindDateInfo().getAdditionalReservationEndDate())
                .build();
    }
}
