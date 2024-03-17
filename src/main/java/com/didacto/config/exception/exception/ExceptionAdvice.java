package com.didacto.config.exception.exception;

import com.didacto.config.exception.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
//@RestController
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ExceptionAdvice.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleDefaultException(Exception e) {
        logger.error("Internal Server Error", e);
        return ErrorCode.UNKNOWN.toErrorResponseEntity();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgumentException(ServerWebInputException e) {
        return ErrorCode.BAD_REQUEST.toErrorResponseEntity();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> badRequestExceptionHandler() {
        return ErrorCode.BAD_REQUEST.toErrorResponseEntity();
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonResponse<Void>> commonExceptionHandler(CommonException e) {
        return e.getErrorCode().toErrorResponseEntity(e.getArgs());
    }

}
