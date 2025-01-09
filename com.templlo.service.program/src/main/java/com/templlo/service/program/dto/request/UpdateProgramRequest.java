package com.templlo.service.program.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record UpdateProgramRequest(

        @Size(max = 100, message = "프로그램 제목은 최대 100자까지 입력 가능합니다.")
        @NotBlank
        String title,

        @Size(max = 1000, message = "설명은 최대 1000자까지 입력 가능합니다.")
        @NotBlank
        String description,

        @NotNull
        LocalTime programStartAt

) {
}
