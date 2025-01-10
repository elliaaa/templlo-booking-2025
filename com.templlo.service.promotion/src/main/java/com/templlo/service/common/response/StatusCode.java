package com.templlo.service.common.response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
	String getName();

	HttpStatus getHttpStatus();

	String getMessage();
}