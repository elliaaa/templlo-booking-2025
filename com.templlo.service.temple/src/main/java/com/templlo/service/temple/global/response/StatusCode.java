package com.templlo.service.temple.global.response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    String getName();
    HttpStatus getHttpStatus();
    String getMessage();
}
