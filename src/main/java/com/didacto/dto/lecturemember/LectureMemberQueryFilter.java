package com.didacto.dto.lecturemember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureMemberQueryFilter {
    private Long lectureId;
    private Long memberId;
    private Boolean deleted;
}
