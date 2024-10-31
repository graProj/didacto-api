package com.didacto.config.exception.custom.exception;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.BasicCustomException500;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 409 : 이미 존재하는 자원과 중복될 때
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistElementException409 extends BasicCustomException500 {
    public AlreadyExistElementException409(ErrorDefineCode code) {
        super(code);
    }
}