package com.templlo.service.reservation.domain.reservation.controller.exception;

import com.templlo.service.reservation.global.common.response.StatusCode;
import com.templlo.service.reservation.global.common.exception.BaseException;

public class ReservationException extends BaseException {
    public ReservationException(StatusCode code) {
        super(code);
    }
}
