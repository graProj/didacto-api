package com.didacto.dto.example;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

//TODO : Swagger API Docs 설정 이후
// API 문서 설명 내용(API 용도, 요청 및 응답 모델의 필드 설명, 성공 및 실패 케이스 정의 등에 대한 문서 작성 코드를 추가한다.)

public class ExampleResponseDto {

    private Long examId;
    private String name;

}
