package com.templlo.service.review.common.exception.baseException;

import com.templlo.service.review.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {

	public NotFoundException() {
		super(ErrorCode.NOT_FOUND);
	}
}
