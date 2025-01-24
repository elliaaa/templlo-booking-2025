package com.templlo.service.reservation.domain.reservation.client.model.response.wrapper;

public record UserServiceWrapperRes<T> (
        String status,
        String code,
        String message,
        T data
) {

}
