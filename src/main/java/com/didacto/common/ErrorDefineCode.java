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
    MEMBER_NOT_FOUND("ERR_11","회원을 찾을 수 없습니다."),
    MEMBER_UNRESISTER("ERR_12","탈퇴된 회원입니다."),
    USER_NOT_FOUND("ENROLL_1", "해당 사용자를 찾을 수 없습니다."),
    ALREADY_ENROLL_REQUEST("ENROLL_2", "이미 교수자에게 강의 등록 요청을 보냈습니다."),
    ALREADY_JOIN("ENROLL_3", "이미 강의에 등록된 상태입니다."),
    ALREADY_ENROLL("ENROLL_4", "등록 요청에 대한 처리가 이미 완료되었습니다. 혹은 해당 등록 처리에 대한 사용자의 권한이 없습니다."),
    NOT_FOUND_ENROLL("ENROLL_5", "해당 초대 정보를 찾을 수 없습니다."),
    CONFIRM_FAIL_USER_DELETED("ENROLL_6", "등록 요청을 한 사용자가 탈퇴하였습니다. 해당 요청은 취소 처리됩니다."),
    LECTURE_MEMBER_NOT_FOUND("LMB_01", "강의 참여자를 찾을 수 없습니다."),
    LECTURE_MEMBER_ALREADY_EXISTENCE("LMB_02", "이미 강의에 등록된 사용자입니다."),
    LECTURE_MEMBER_FREETEER_OVERCOUNT_3("LMB_03", "FREETEER의 최대 생성 강의  갯수는 3개 입니다."),
    LECTURE_MEMBER_PREMIUM_OVER("LMB_04", "PREMIUM 구독기간이 만료되었습니다."),
    DELETED_LECTURE("LECTURE_01", "삭제된 강의입니다."),
    ORDER_GRADE_FAIL("ORDER_01","주문된 상품이 프리미엄이 아닙니다."),
    ORDER_NOT_FOUND("ORDER_02", "주문을 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND("PAYMENT_01", "결제 완료된 상품이 없습니다.")
    ;

    private final String code;
    private final String message;

}
