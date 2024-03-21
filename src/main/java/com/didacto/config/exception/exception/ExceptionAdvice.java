package com.didacto.config.exception.exception;

import com.didacto.config.exception.response.CommonResponse;
import com.didacto.config.exception.response.ErrorResponse;
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



    // 500 에러
    public CommonResponse illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        logger.info("e = {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "500", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return errorResponse.toFailResponse();
    }

    // 401 응답
    // 아이디 혹은 비밀번호 오류시 발생
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse loginFailureException(LoginFailureException e) {
        logger.error("Login failure: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("로그인에 실패하였습니다.", "401", HttpStatus.UNAUTHORIZED.value());
        return errorResponse.toFailResponse();
    }

    // 409 응답
    // username 중복
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse memberEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        logger.error("Email already exists: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage() + "은 중복된 아이디 입니다.", "409", HttpStatus.CONFLICT.value());
        return errorResponse.toFailResponse();
    }

}
