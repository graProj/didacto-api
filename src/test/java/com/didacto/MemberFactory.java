package com.didacto;

import com.didacto.common.BaseEntity;
import com.didacto.domain.Authority;
import com.didacto.domain.Member;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class MemberFactory extends BaseEntity {

    public static Member createMember(Long id, String email, String name, String password, String birth, Authority role) {
        Member member = Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .birth(parseBirth(birth))
                .role(role)
                .deleted(false)
                .build();
        return member;
    }

    private static OffsetDateTime parseBirth(String birthString) {
        // 생년월일 문자열을 OffsetDateTime으로 파싱하는 로직을 구현해야 함
        return OffsetDateTime.now(); // 임시로 현재 시간을 반환하는 예시
    }
}
