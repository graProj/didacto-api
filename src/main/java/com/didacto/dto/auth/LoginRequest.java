package com.didacto.dto.auth;

import com.didacto.domain.Authority;
import com.didacto.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(title = "로그인 : Request 스키마")  // Swagger Docs 표시 : 스키마 설명
public class LoginRequest {
    @NotBlank(message = "{LoginRequestDto.email.notBlank}")
    @Schema(description = "이메일", example = "abc123@naver.com")
    private String email;

    @NotBlank(message = "{LoginRequestDto.password.notBlank}")
    @Schema(description = "비밀번호", example = "abcasdj456789!!")
    private String password;




    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}