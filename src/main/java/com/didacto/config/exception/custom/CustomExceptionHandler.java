package com.didacto.config.exception.custom;

import com.didacto.config.exception.custom.exception.AuthForbiddenException;
import com.didacto.config.exception.global.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@Slf4j(topic = "EXCEPTION_HANDLER")
@RestControllerAdvice
public class CustomExceptionHandler extends GlobalExceptionHandler {
    // 403 Auth Forbidden Exception
    @ExceptionHandler(AuthForbiddenException.class)
    @Hidden
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403 Forbidden
    public ResponseEntity<Object> handleAuthForbiddenException(AuthForbiddenException exception, WebRequest request) {
        log.error("Forbidden : ", exception);
        return buildErrorResponse(exception, exception.getCode(), HttpStatus.FORBIDDEN, request);
    }



    /** 필요시 ExceptionHandler 추가 - 예상가는 오류 있다면 전부 ExceptionHandler 이용해 처리. **/

}
