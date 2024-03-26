package com.didacto.dto.example;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(title = "Example : Response 스키마")  // Swagger Docs 표시 : 스키마 설명
public class ExampleResponse {

    @Schema(description = "PK", example = "1") //Swagger Docs 표시 : 상세 필드 설명
    private Long examId;

    @Schema(description = "이름", example = "blah blah") //Swagger Docs 표시 : 상세 필드 설명
    private String name;

}
