package com.templlo.service.temple.common.exception;

import com.templlo.service.temple.common.response.StatusCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final StatusCode code;

    public BaseException(StatusCode code) {
        this.code = code;
    }
}
