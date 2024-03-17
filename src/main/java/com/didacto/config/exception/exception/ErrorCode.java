package com.didacto.config.exception.exception;

import com.didacto.config.exception.response.CommonResponse;
import com.didacto.config.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public enum ErrorCode {
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "E-CMN-0000", "알 수 없는 오류가 발생 했습니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "E-CMN-0001", "잘못된 요청입니다"),
    INTERNAL_ERROR_RELAY(HttpStatus.BAD_REQUEST, "E-CMN-0100", ""),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ResponseEntity<CommonResponse<Void>> toErrorResponseEntity() {
        ErrorResponse errorResponse = toErrorResponse();
        CommonResponse<Void> commonResponse = errorResponse.toFailResponse();
        return ResponseEntity.status(getStatus()).body(commonResponse);
    }

    public ResponseEntity<CommonResponse<Void>> toErrorResponseEntity(List<String> args) {
        ErrorResponse errorResponse = toErrorResponse(args);
        CommonResponse<Void> commonResponse = errorResponse.toFailResponse();
        return ResponseEntity.status(getStatus()).body(commonResponse);
    }

    public ErrorResponse toErrorResponse(List<String> args) {
        int status = getStatus().value();
        String code = getCode();
        String message = args != null ?
                args.stream().reduce(getMessage(), (a, c) -> a.replace("{" + args.indexOf(c) + "}", c), (x, y) -> y) :
                getMessage();
        return new ErrorResponse(message, code, status); // ErrorResponse 클래스의 인스턴스를 생성하여 반환
    }

    public ErrorResponse toErrorResponse() {
        return toErrorResponse(null);
    }
}

