package com.templlo.service.temple.global.exception;

import com.templlo.service.temple.global.response.StatusCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final StatusCode code;

    public BaseException(StatusCode code) {
        this.code = code;
    }
}
