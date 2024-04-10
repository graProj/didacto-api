package com.didacto.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "회원정보 수정 : Request 스키마")  // Swagger Docs 표시 : 스키마 설명
public class MemberModificationRequest {

    @NotBlank(message = "변경할 비밀번호를 입력해주세요.")
    @Schema(description = "비밀번호", example = "gildong123!@")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "변경할 사용자 이름을 입력해주세요.")
    @Schema(description = "이름", example = "홍길동")
    @Size(min = 2, message = "사용자 이름이 너무 짧습니다.")
    private String name;

    @NotBlank(message = "변경할 생년월일을 입력해 주세요")
    @Schema(description = "생년월일", example = "20000520")
    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "생년월일은 yyyyMMdd 형식으로 입력해야 합니다.")
    private String birth;

}