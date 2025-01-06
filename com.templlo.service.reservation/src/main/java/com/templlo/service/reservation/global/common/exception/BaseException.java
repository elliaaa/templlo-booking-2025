package com.templlo.service.reservation.global.common.exception;

import com.templlo.service.reservation.global.common.response.StatusCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final StatusCode code;

    public BaseException(StatusCode code) {
        this.code = code;
    }

}
