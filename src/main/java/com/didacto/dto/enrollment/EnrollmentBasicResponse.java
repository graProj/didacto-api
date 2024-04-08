package com.didacto.dto.enrollment;

import com.didacto.domain.EnrollmentStatus;
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
public class EnrollmentBasicResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "WAITING || CANCELLED || ACCEPTED || REJECTED")
    private EnrollmentStatus status;

    @Schema(example = "1")
    private Long lecture_id;

    @Schema(example = "1")
    private Long member_id;

}


