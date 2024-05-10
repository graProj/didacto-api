package com.didacto.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureQueryFilter {
    @Schema(description = "강의명으로 검색합니다.")
    private String titleKeyword;
    private Boolean deleted;
}

