package com.templlo.service.program.global.common.exception;


import com.templlo.service.program.global.common.response.StatusCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final StatusCode code;

    public BaseException(StatusCode code) {
        this.code = code;
    }

}
