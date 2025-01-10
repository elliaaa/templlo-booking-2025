package com.templlo.service.program.dto.response;

import com.templlo.service.program.entity.Program;
import com.templlo.service.program.entity.ProgramGenderStatus;
import com.templlo.service.program.entity.ProgramStatus;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record BlindDateScheduleResponse(

        UUID programId,
        LocalDate programDate,
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

) implements ProgramScheduleResponse {

    public static BlindDateScheduleResponse from(Program program) {
        return BlindDateScheduleResponse.builder()
                .programId(program.getId())
                .programDate(program.getBlindDateInfo().getProgramDate())
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
