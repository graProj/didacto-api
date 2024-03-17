package com.didacto.config.exception.exception;

import java.util.List;

public class InternalErrorRelayException extends CommonException {
    public InternalErrorRelayException(List<String> args) {
        super(ErrorCode.INTERNAL_ERROR_RELAY, args);
    }
}
