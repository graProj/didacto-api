package com.didacto.dto.member;

import com.didacto.domain.Authority;
import com.didacto.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private OffsetDateTime birth;
    private Authority role;
    private OffsetDateTime created_date;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.birth = member.getBirth();
        this.role = member.getRole();
        this.created_date = member.getCreatedTime();
    }
}
