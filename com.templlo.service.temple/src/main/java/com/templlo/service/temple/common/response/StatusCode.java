package com.templlo.service.temple.common.response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    String getName();
    HttpStatus getHttpStatus();
    String getMessage();
}
