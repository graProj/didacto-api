package com.didacto;

import com.didacto.domain.Authority;
import com.didacto.domain.Member;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class MemberFactory {

    public static Member createMember(){
        Member member = Member.builder()
                .id(5L)
                .email("gildong@naver.com")
                .name("홍길동")
                .password("gildong123!!")
                .birth(parseBirth("20000621"))
                .role(Authority.ROLE_USER)
                .build();
        return member;
    }

    private static OffsetDateTime parseBirth(String birth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birth, formatter);
        return birthDate.atStartOfDay().atOffset(ZoneOffset.UTC);
    }
}