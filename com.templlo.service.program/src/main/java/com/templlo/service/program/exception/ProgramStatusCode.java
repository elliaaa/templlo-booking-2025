package com.templlo.service.program.exception;

import com.templlo.service.program.global.common.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProgramStatusCode implements StatusCode {

    // 성공
    SUCCESS_PROGRAM_CREATE(HttpStatus.CREATED, "프로그램 생성이 성공하였습니다."),
    SUCCESS_PROGRAM_READ(HttpStatus.OK, "프로그램 조회가 성공하였습니다."),


    // 실패
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 프로그램을 찾을 수 없습니다."),
    TEMPLE_STAY_DAILY_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜의 템플 스테이 정보를 찾을 수 없습니다."),
    BLIND_DATE_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 블라인드 소개팅의 정보를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}