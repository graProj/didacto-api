package com.didacto.common;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDefineCode {

    UNCAUGHT("ERR_00", "Uncaught Exception"),
    VALID_ERROR("ERR_01", "Field Validation fail"),
    EXAMPLE_OCCURE_ERROR("ERR_02", "예제 코드에서 그냥 발생시킨 오류랍니다"),
    DUPLICATE_EXAMPLE_NAME("ERR_03", "Example로 중복된 이름을 사용할 수 없습니다"),
    AUTH_NOT_FOUND_EMAIL("ERR_04", "해당 이메일을 찾을 수 없습니다."),
    AUTH_NMATCH_PWD("ERR_05", "비밀번호가 일치하지 않습니다."),
    ALREADY_EXIST_EMAIL("ERR_06", "이미 존재하는 이메일입니다."),
    LECTURE_NOT_FOUND("ERR_07", "해당 강의를 찾을 수 없습니다."),
    AUTHORIZATION_FAIL("ERR_08", "해당 권한이 없습니다."),
    AUTHENTICATE_FAIL("ERR_09", "권한 인증에 실패했습니다."),
    AUTH_AUTHORITY_FAIL("ERR_10","USER와 ADMIN중 선택해야합니다."),
    MEMBER_NOT_FOUND("ERR_11","회원을 찾을 수 없습니다.")
    ;

    private final String code;
    private final String message;

}
