package com.templlo.service.program.dto.request;

import com.templlo.service.program.entity.ProgramStatus;
import com.templlo.service.program.entity.ProgramType;
import com.templlo.service.program.validation.ValidCreateProgramRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@ValidCreateProgramRequest
public record CreateProgramRequest(

        @NotNull(message = "프로그램의 사찰 입력은 필수입니다.")
        UUID templeId,

        @NotBlank(message = "프로그램의 제목 입력은 필수입니다.")
        @Size(max = 100, message = "프로그램 제목은 최대 100자까지 입력 가능합니다.")
        String title,

        @NotBlank(message = "프로그램의 설명 입력은 필수입니다.")
        @Size(max = 1000, message = "설명은 최대 1000자까지 입력 가능합니다.")
        String description,

        @NotNull(message = "프로그램의 타입 입력은 필수입니다.")
        ProgramType type,

        Integer programFee,

        @NotNull(message = "프로그램의 시작 시간 입력은 필수입니다.")
        LocalTime programStartAt,

        @NotNull(message = "프로그램의 예약 시작 날짜 입력은 필수입니다.")
        LocalDate reservationStartDate,

        @NotNull(message = "프로그램 예약 종료 날짜 입력은 필수입니다.")
        LocalDate reservationEndDate,

        @NotNull(message = "프로그램의 예약 활성화 여부는 필수입니다.")
        ProgramStatus programStatus,

        List<String> programDays, // TEMPLE_STAY 전용 필드, BLIND_DATE 일 때는 []

        Integer programCapacity, // TEMPLE_STAY 전용 필드

        LocalDate programDate, // BLIND_DATE 전용 필드

        Integer maleCapacity, // BLIND_DATE 전용 필드

        Integer femaleCapacity // BLIND_DATE 전용 필드
) {

}
