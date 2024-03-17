package com.didacto.dto.example;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//TODO : Swagger API Docs 설정 이후
// API 문서 설명 내용(API 용도, 요청 및 응답 모델의 필드 설명, 성공 및 실패 케이스 정의 등에 대한 문서 작성 코드를 추가한다.)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleRequestDto {

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    private String name;

}
