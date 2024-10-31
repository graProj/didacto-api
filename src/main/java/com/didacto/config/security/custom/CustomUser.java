package com.didacto.config.security.custom;

import com.didacto.domain.Authority;
import com.didacto.domain.Grade;
import com.didacto.domain.Member;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUser {
    private Long id;

    private String email;

    private String password;

    private Authority role;
    // Member 객체를 받는 생성자 추가

    private Grade grade;

    public CustomUser(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.role = member.getRole(); // 사용자 권한으로 설정하거나 필요에 따라 다르게 설정할 수 있습니다.
        this.grade = member.getGrade();
    }
}
