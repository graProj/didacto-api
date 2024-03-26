package com.didacto.config.exception.custom;

import com.didacto.common.ErrorDefineCode;
import lombok.Getter;

@Getter
public class BasicCustomException500 extends RuntimeException {
    private ErrorDefineCode code;

    public BasicCustomException500(ErrorDefineCode code) {
        super(code.getMessage());
        this.code = code;

    }
}


