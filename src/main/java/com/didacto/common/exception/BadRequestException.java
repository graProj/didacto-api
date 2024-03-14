package com.didacto.common.exception;

public class BadRequestException extends CommonException {
    public BadRequestException() {
        super(ErrorCode.BAD_REQUEST);
    }
}
