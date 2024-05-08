package com.didacto;

import com.didacto.common.BaseEntity;
import com.didacto.domain.Authority;
import com.didacto.domain.Member;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class MemberFactory extends BaseEntity {

    public static Member createMember() {
        Member member = Member.builder()
                .id(5L)
                .email("gildong@naver.com")
                .name("홍길동")
                .password("gildong123!!")
                .birth(parseBirth("20000621"))
                .role(Authority.ROLE_USER)
                .deleted(false)
                .build();
        return member;
    }

    private static OffsetDateTime parseBirth(String birthString) {
        // 생년월일 문자열을 OffsetDateTime으로 파싱하는 로직을 구현해야 함
        return OffsetDateTime.now(); // 임시로 현재 시간을 반환하는 예시
    }
}
