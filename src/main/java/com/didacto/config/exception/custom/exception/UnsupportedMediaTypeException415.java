package com.didacto.config.exception.custom.exception;


import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.BasicCustomException500;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 415 : 요청 파일이 허용되지 않는 미디어 타입일 경우 (사진만 허용되는데 다른거 보낸경우)
 */

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedMediaTypeException415 extends BasicCustomException500 {

    public UnsupportedMediaTypeException415(ErrorDefineCode code) {
        super(code);
    }
}
