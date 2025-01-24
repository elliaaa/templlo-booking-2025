package com.templlo.service.reservation.domain.reservation.domain;

public enum ReservationStatus {
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELED,
    REJECTED,
    PROCESSING_CANCEL,
    PROCESSING_REJECT,
    CANCEL_FAILED,
    REJECT_FAILED
}
