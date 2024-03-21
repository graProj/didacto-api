package com.didacto.config.exception.custom.exception;


import com.didacto.common.ErrorDefineCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
public class AuthForbiddenException extends RuntimeException {

    private ErrorDefineCode code;

    public AuthForbiddenException(ErrorDefineCode code) {
        super(code.getMessage());
        this.code = code;

    }
}
