package com.templlo.service.reservation.domain.reservation.controller.exception;

import com.templlo.service.reservation.global.common.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationStatusCode implements StatusCode {
    SUCCESS_RESERVATION_CREATE(HttpStatus.CREATED, "예약 신청 성공"),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id 에 해당하는 예약을 찾을 수 없음")

    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
