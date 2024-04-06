package com.didacto.dto.member;
import com.didacto.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberFindResponse {

    private String email;
    private String name;

    public static MemberFindResponse toDto(Member member) {
        return new MemberFindResponse(member.getEmail(), member.getName());
    }
}
