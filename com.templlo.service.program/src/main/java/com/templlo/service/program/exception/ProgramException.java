package com.templlo.service.program.exception;

import com.templlo.service.program.global.common.exception.BaseException;
import com.templlo.service.program.global.common.response.StatusCode;
import jdk.jshell.Snippet;

public class ProgramException extends BaseException {
    public ProgramException(StatusCode code) {
        super(code);
    }
}
