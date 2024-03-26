package com.didacto.common;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDefineCode {

    UNCAUGHT("ERR_00", "Uncaught Exception"),
    VALID_ERROR("ERR_01", "Field Validation fail"),
    EXAMPLE_OCCURE_ERROR("ERR_02", "예제 코드에서 그냥 발생시킨 오류랍니다"),
    DUPLICATE_EXAMPLE_NAME("ERR_03", "Example로 중복된 이름을 사용할 수 없습니다")

    ;

    private final String code;
    private final String message;

}
