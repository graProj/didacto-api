package com.didacto.config.exception.custom;

import com.didacto.config.exception.custom.exception.AlreadyExistElementException409;
import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.config.exception.custom.exception.ForbiddenException403;
import com.didacto.config.exception.global.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@Slf4j(topic = "EXCEPTION_HANDLER")
@RestControllerAdvice
public class CustomExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(BasicCustomException500.class)
    @Hidden
    public ResponseEntity<Object> handleAlreadyExistElementException(BasicCustomException500 exception, WebRequest request) {
        String classname = exception.getClass().getSimpleName();
        log.error(classname + " : " + exception.getMessage());
        int statusCode = Integer.parseInt(classname.substring(classname.length() - 3));
        return buildErrorResponse(exception, exception.getCode(), HttpStatus.valueOf(statusCode), request);
    }



}
