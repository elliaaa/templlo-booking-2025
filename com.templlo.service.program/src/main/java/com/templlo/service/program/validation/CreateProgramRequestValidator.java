package com.templlo.service.program.validation;

import com.templlo.service.program.dto.request.CreateProgramRequest;
import com.templlo.service.program.entity.ProgramType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class CreateProgramRequestValidator implements ConstraintValidator<ValidCreateProgramRequest, CreateProgramRequest> {

    @Override
    public boolean isValid(CreateProgramRequest request, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (request.type() == ProgramType.TEMPLE_STAY) {
            // TEMPLE_STAY 전용 필드 검증
            if (request.programDays() == null || request.programDays().isEmpty()) {
                addConstraintViolation(context, "TEMPLE_STAY는 프로그램 요일 입력이 필수입니다.", "programDays");
                isValid = false;
            }
            if (request.programCapacity() == null) {
                addConstraintViolation(context, "TEMPLE_STAY는 프로그램 정원 입력이 필수입니다.", "programCapacity");
                isValid = false;
            }
            if (request.programFee() == null || request.programFee() < 0 ) {
                addConstraintViolation(context, "TEMPLE_STAY는 참가 비용 0원 이상 입력이 필수입니다.", "programFee");
                isValid = false;
            }
            // TEMPLE_STAY 일 때 BLIND_DATE 필드 입력 시
            if (request.maleCapacity() != null || request.femaleCapacity() != null) {
                addConstraintViolation(context, "TEMPLE_STAY는 성별 정원의 입력이 불가능합니다.", "maleCapacity|femaleCapacity");
                isValid = false;
            }
            if (request.programDate() != null) {
                addConstraintViolation(context, "TEMPLE_STAY는 예약 시작일 ~ 예약 종료일, 요일 주기 별로 자동 생성되므로 프로그램 날짜의 입력이 불가합니다.", "programDate");
                isValid = false;
            }
        } else if (request.type() == ProgramType.BLIND_DATE) {
            // BLIND_DATE 전용 필드 검증
            if (request.programDate() == null) {
                addConstraintViolation(context, "BLIND_DATE는 프로그램 날짜 입력이 필수입니다.", "programDate");
                isValid = false;
            }
            if (request.maleCapacity() == null || request.maleCapacity() <= 0) {
                addConstraintViolation(context, "BLIND_DATE는 남성 정원 1명 이상 입력이 필수입니다.", "maleCapacity");
                isValid = false;
            }
            if (request.femaleCapacity() == null || request.femaleCapacity() <= 0) {
                addConstraintViolation(context, "BLIND_DATE는 여성 정원 1명 이상 입력이 필수입니다.", "femaleCapacity");
                isValid = false;
            }
            if (request.programFee() != 0) {
                addConstraintViolation(context, "BLIND_DATE는 가격이 0원 이어야 합니다.", "programFee");
                isValid = false;
            }
            // BLIND_DATE 일 때 TEMPLE_STAY 필드 입력 시
            if (request.programDays() != null && !request.programDays().isEmpty()) {
                addConstraintViolation(context, "BLIND_DATE는 요일 주기의 입력이 불가능합니다.", "programDays");
                isValid = false;
            }
            if (!request.reservationEndDate().isBefore(request.programDate())) {
                addConstraintViolation(context, "프로그램 시작 날짜는 예약 종료일 이후여야 합니다.", "programDate");
                isValid = false;
            }

        }

        if (request.reservationStartDate() != null && request.reservationEndDate() != null) {
            if (request.reservationEndDate().isBefore(request.reservationStartDate())) {
                addConstraintViolation(context, "예약 종료 날짜는 예약 시작 날짜 이후로 입력이 가능합니다..", "reservationEndDate");
                isValid = false;
            }
            if (LocalDate.now().isAfter(request.reservationStartDate())) {
                addConstraintViolation(context, "예약 시작 날짜는 현재 날짜 이후로 입력이 가능합니다.", "reservationStartDate");
                isValid = false;
            }
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