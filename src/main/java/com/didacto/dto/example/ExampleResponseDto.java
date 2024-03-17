package com.didacto.dto.example;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleResponseDto {
    private Long examId;
    private String name;
}
