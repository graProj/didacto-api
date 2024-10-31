package com.didacto.config.exception.custom.exception;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.BasicCustomException500;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 404 : 파일을 찾지 못해 다운로드에 실패한 경우
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundException404 extends BasicCustomException500 {
    public FileNotFoundException404(ErrorDefineCode code) {
        super(code);
    }
}