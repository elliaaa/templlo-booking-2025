package com.templlo.service.program.validation;

import com.templlo.service.program.dto.request.UpdateProgramScheduleRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateProgramScheduleRequestValidator implements ConstraintValidator<ValidUpdateProgramScheduleRequest, UpdateProgramScheduleRequest> {

    @Override
    public boolean isValid(UpdateProgramScheduleRequest request, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (request.additionalReservationStartDate() != null && request.additionalReservationEndDate() == null
        || request.additionalReservationStartDate() == null && request.additionalReservationEndDate() != null) {
            addConstraintViolation(context, "추가 예약 시작, 종료 날짜를 모두 입력해야 합니다..", "additionalReservationStartDate, additionalReservationEndDate");
            isValid = false;
        }

        return isValid;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String property) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
