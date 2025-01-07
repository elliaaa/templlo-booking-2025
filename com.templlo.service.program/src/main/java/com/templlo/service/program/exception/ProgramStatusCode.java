package com.templlo.service.program.exception;

import com.templlo.service.program.global.common.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProgramStatusCode implements StatusCode {

    SUCCESS_PROGRAM_CREATE(HttpStatus.CREATED, "프로그램 생성이 성공되었습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}