package com.didacto.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureModificationRequest {
    @Schema(example = "1")
    private Long lectureId;

    @Schema(example = "수정된 강의 제목")
    private String title;

    @Builder
    public LectureModificationRequest(Long lectureId, String title) {
        this.lectureId = lectureId;
        this.title = title;
    }
}
