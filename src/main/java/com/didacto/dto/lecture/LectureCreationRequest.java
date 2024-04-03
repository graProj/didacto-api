package com.didacto.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter

public class LectureCreationRequest {
    @Schema(example = "컴퓨터 구조")
    private String title;
    @Schema(example = "1")
    private Long ownerId;
}
