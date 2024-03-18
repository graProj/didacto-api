package com.didacto.config.exception.response;


public class CommonResponse<T> {
    private final boolean success;
    private final T response;
    private final ErrorResponse error;

    public CommonResponse(boolean success, T response, ErrorResponse error) {
        this.success = success;
        this.response = response;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResponse() {
        return response;
    }

    public ErrorResponse getError() {
        return error;
    }
}
