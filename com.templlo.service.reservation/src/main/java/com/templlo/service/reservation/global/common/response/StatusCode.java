package com.templlo.service.reservation.global.common.response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    String getName();
    HttpStatus getHttpStatus();
    String getMessage();
}
