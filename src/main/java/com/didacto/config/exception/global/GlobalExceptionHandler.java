package com.didacto.config.exception.global;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonError;
import com.didacto.common.response.CommonResponse;
import com.didacto.config.exception.GlobalExceptionHandlerInterface;
import com.didacto.config.exception.custom.exception.AuthForbiddenException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@Slf4j(topic = "EXCEPTION_HANDLER")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TRACE = "trace";

    @Value("${error.printStackTrace}")
    private boolean printStackTrace;

    @Value("${error.printStackTraceLine}")
    private int printStackTraceLine;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return buildErrorResponse(ex, ErrorDefineCode.UNCAUGHT, HttpStatus.valueOf(statusCode.value()), request);
    }

    protected ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                        ErrorDefineCode errorCode,
                                                        HttpStatus httpStatus,
                                                        WebRequest request) {
        CommonError error = new CommonError(errorCode.getCode(), LocalDateTime.now());
        if (printStackTrace && isTraceOn(exception)) {
            error.setStackTrace(getStackTrace(exception, printStackTraceLine));
        }
        CommonResponse<CommonError> errorResponseDto =
                new CommonResponse(false, httpStatus, errorCode.getMessage(), error);


        return ResponseEntity.status(httpStatus).body(errorResponseDto);
    }

    private String getStackTrace(Exception e, int line){
        String stackTrace = ExceptionUtils.getStackTrace(e);

        // 스택 트레이스를 줄 단위로 분할하여 line줄까지만 사용
        String[] lines = stackTrace.split(System.lineSeparator());
        StringBuilder limitedStackTrace = new StringBuilder();
        int limit = Math.min(lines.length, line);
        for (int i = 0; i < limit; i++) {
            limitedStackTrace.append(lines[i]).append(System.lineSeparator());
        }

        return limitedStackTrace.toString();
    }

    private boolean isTraceOn(Exception exception) {
        if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // 412 Validate Exception
    @Override
    @Hidden
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        CommonError error = new CommonError(ErrorDefineCode.VALID_ERROR.getCode(), LocalDateTime.now());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        CommonResponse<CommonError> errorResponseDto =
                new CommonResponse(false, HttpStatus.UNPROCESSABLE_ENTITY,
                        ErrorDefineCode.VALID_ERROR.getMessage(), error);

        return ResponseEntity.unprocessableEntity().body(errorResponseDto);
    }


    // 500 Uncaught Exception
    @ExceptionHandler(Exception.class)
    @Hidden
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error("Internal error occurred", exception);
        return buildErrorResponse(exception, ErrorDefineCode.UNCAUGHT, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}


