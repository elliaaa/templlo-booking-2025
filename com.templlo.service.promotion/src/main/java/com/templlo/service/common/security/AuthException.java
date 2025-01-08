package com.templlo.service.common.security;

import com.templlo.service.common.response.StatusCode;
import com.thoughtworks.xstream.core.BaseException;

public class AuthException extends BaseException {
	public AuthException(StatusCode code) {
		super(String.valueOf(code));
	}
}