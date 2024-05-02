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
    Long lectureId;
    Long memberId;
    Boolean deleted;
}
