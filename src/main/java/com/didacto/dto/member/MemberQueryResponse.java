package com.didacto.dto.member;
import com.didacto.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberQueryResponse {

    private String email;
    private String name;

    public static MemberQueryResponse toDto(Member member) {
        return new MemberQueryResponse(member.getEmail(), member.getName());
    }
}
