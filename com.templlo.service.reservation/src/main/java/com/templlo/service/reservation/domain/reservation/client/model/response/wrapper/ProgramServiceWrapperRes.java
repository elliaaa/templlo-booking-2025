package com.templlo.service.reservation.domain.reservation.client.model.response.wrapper;

public record ProgramServiceWrapperRes<T> (
        String status,
        String code,
        String message,
        T data
) {

}
