package com.didacto.dto.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Schema(title = "회원가입 : Request 스키마")  // Swagger Docs 표시 : 스키마 설명
public class SignUpRequestDto {

    @NotBlank(message = "이메일 입력해주세요.")
    @Schema(description = "이메일", example = "abc123@naver.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "비밀번호", example = "abcasdj456789!!")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    @Schema(description = "이름", example = "홍길동")
    @Size(min = 2, message = "사용자 이름이 너무 짧습니다.")
    private String name;

}