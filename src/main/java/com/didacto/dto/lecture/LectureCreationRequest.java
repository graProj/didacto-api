package com.didacto.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureCreationRequest {
    @Schema(example = "강의 제목")
    private String title;

    @Builder
    public LectureCreationRequest(String title) {
        this.title = title;
    }
}
