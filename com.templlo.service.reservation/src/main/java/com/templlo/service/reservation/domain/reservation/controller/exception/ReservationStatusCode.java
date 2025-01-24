package com.templlo.service.reservation.domain.reservation.controller.exception;

import com.templlo.service.reservation.global.common.response.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationStatusCode implements StatusCode {
    CREATE_RESERVATION_SUCCESS(HttpStatus.CREATED, "예약 신청에 성공했습니다"),

    GET_RESERVATION_SUCCESS(HttpStatus.OK, "예약 단건 조회에 성공했습니다"),

    GET_RESERVATIONS_OF_USER_SUCCESS(HttpStatus.OK, "나의 예약 신청내역 조회에 성공했습니다"),
    GET_RESERVATIONS_OF_TEMPLE_SUCCESS(HttpStatus.OK, "사찰의 예약 수신내역 조회에 성공했습니다"),
    SEARCH_RESERVATIONS_SUCCESS(HttpStatus.OK, "예약 검색에 성공했습니다."),

    CANCEL_RESERVATION_SUCCESS(HttpStatus.OK, "예약 취소 신청에 성공했습니다"),
    REJECT_RESERVATION_SUCCESS(HttpStatus.OK, "예약 거절 신청에 성공했습니다"),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id 의 예약을 찾을 수 없습니다"),
    NOT_TEMPLE_OWNER(HttpStatus.FORBIDDEN, "해당 사찰에 대한 권한이 부족합니다");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
