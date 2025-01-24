package com.templlo.service.reservation.domain.reservation.client.model.response;

public record UserServiceWrapperRes<T> (
        String status,
        String code,
        String message,
        T data
) {

}
