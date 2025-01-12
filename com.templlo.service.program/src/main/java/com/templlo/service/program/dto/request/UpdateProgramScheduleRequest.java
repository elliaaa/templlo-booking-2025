package com.templlo.service.program.dto.request;

import com.templlo.service.program.entity.ProgramStatus;
import com.templlo.service.program.validation.ValidUpdateProgramScheduleRequest;

import java.time.LocalDate;

@ValidUpdateProgramScheduleRequest
public record UpdateProgramScheduleRequest(
        ProgramStatus status,
        LocalDate additionalReservationStartDate,
        LocalDate additionalReservationEndDate
) {
}
