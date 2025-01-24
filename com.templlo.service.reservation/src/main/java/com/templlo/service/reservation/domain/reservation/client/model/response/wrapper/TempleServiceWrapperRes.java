package com.templlo.service.reservation.domain.reservation.client.model.response.wrapper;

public record TempleServiceWrapperRes<T> (
        String status,
        String code,
        String message,
        T data
) {

}
