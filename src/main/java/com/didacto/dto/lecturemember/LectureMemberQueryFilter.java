package com.didacto.dto.lecturemember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureMemberQueryFilter {
    @Schema(example = "1")
    private Long lectureId;

    @Schema(example = "1")
    private Long memberId;

    @Schema(example = "false")
    private Boolean deleted;
}
