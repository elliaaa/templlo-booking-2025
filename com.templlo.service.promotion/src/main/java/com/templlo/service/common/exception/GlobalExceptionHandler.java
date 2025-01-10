package com.templlo.service.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.templlo.service.common.response.ApiResponse;
import com.templlo.service.common.response.BasicStatusCode;
import com.templlo.service.common.response.StatusCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// 커스텀 예외 클래스
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<Object> handleCustomBaseException(BaseException e) {
		return handleException(e.getCode());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleAllRuntimeException(RuntimeException e) {
		e.printStackTrace();
		return handleException(BasicStatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	private ResponseEntity<Object> handleException(StatusCode code) {
		return ResponseEntity
			.status(code.getHttpStatus())
			.body(ApiResponse.of(code));
	}

	private ResponseEntity<Object> handleException(StatusCode code, String message) {
		ApiResponse errorResponse = ApiResponse.of(BasicStatusCode.INTERNAL_SERVER_ERROR, message);
		return ResponseEntity
			.status(code.getHttpStatus())
			.body(errorResponse);
	}
}