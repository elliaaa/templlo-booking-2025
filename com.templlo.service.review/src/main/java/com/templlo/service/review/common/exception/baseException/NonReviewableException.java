package com.templlo.service.review.common.exception.baseException;

import com.templlo.service.review.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class NonReviewableException extends BaseException {

	public NonReviewableException() {
		super(ErrorCode.NON_REVIEWABLE);
	}
}
