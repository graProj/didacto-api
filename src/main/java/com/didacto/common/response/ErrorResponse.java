package com.didacto.common.response;

public class ErrorResponse {
    private final String message;
    private final String code;
    private final int status;

    public ErrorResponse(String message, String code, int status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public CommonResponse<Void> toFailResponse() {
        return new CommonResponse<Void>(false, null, this);
    }
}
