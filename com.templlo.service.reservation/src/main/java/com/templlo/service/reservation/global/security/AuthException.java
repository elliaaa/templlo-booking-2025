package com.templlo.service.reservation.global.security;

import com.templlo.service.reservation.global.common.exception.BaseException;
import com.templlo.service.reservation.global.common.response.StatusCode;

public class AuthException extends BaseException {
    public AuthException(StatusCode code) {
        super(code);
    }
}
