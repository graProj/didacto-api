package com.didacto.config.exception.custom.exception;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.BasicCustomException500;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 503 : 네트워크 오류가 발생했을 경우
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NetworkException503 extends BasicCustomException500 {
    public NetworkException503(ErrorDefineCode code) {super(code); }
}
