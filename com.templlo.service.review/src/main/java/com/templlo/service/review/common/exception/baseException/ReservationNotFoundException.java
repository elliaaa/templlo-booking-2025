package com.templlo.service.review.common.exception.baseException;

import com.templlo.service.review.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class ReservationNotFoundException extends BaseException {

	public ReservationNotFoundException() {
		super(ErrorCode.RESERVATION_NOT_FOUND);
	}
}
