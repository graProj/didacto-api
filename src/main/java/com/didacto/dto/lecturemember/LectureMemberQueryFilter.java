package com.didacto.dto.lecturemember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureMemberQueryFilter {
    private Long lectureId;
    private Long memberId;
    private List<Long> memberIds;
    private Boolean deleted;
}
