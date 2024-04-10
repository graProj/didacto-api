package com.didacto.dto.member;
import com.didacto.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private String email;
    private String name;

    public static MemberResponse toDto(Member member) {
        return new MemberResponse(member.getEmail(), member.getName());
    }
}
