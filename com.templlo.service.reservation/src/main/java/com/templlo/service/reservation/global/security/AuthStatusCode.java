package com.templlo.service.reservation.global.security;

import com.templlo.service.reservation.global.common.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthStatusCode implements StatusCode {
    USER_INFO_REQUIRED(HttpStatus.UNAUTHORIZED, "사용자 인증정보가 올바르게 전달되지 않음"),
    USER_ROLE_INVALID(HttpStatus.UNAUTHORIZED, "사용자 권한 정보가 유효하지 않음"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
