package com.templlo.service.temple.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BasicStatusCode implements StatusCode{

    OK(HttpStatus.OK, "API 요청에 성공했습니다"),
    TEMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "사찰을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"지역이 필요합니다.");


    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String getName() {
        return this.name();
    }
}
