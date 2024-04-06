package com.didacto.dto.enrollment;

import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "Enrollment : 기본 데이터 Response")
public class EnrollmentBasicTypeResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "WAITING || CANCELLED || ACCEPTED ||REJECTED")
    private EnrollmentStatus status;

    @Schema(example = "1")
    private Long lecture_id;

    @Schema(example = "1")
    private Long member_id;

}


