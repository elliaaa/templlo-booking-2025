package com.templlo.service.program.dto.response;

import com.templlo.service.program.entity.BlindDateInfo;
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

    public static BlindDateScheduleResponse from(Program program, BlindDateInfo blindDateInfo) {
        return BlindDateScheduleResponse.builder()
                .programId(program.getId())
                .programDate(blindDateInfo.getProgramDate())
                .status(blindDateInfo.getStatus())
                .genderStatus(blindDateInfo.getGenderStatus())
                .programStartAt(program.getProgramStartAt())
                .programCapacity(program.getProgramCapacity())
                .availableMaleCapacity(blindDateInfo.getAvailableMaleCapacity())
                .availableFemaleCapacity(blindDateInfo.getAvailableFemaleCapacity())
                .reservationStartDate(program.getReservationStartDate())
                .reservationEndDate(program.getReservationEndDate())
                .additionalReservationStartDate(blindDateInfo.getAdditionalReservationStartDate())
                .additionalReservationEndDate(blindDateInfo.getAdditionalReservationEndDate())
                .build();
    }
}
