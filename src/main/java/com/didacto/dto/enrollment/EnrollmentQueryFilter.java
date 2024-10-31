package com.didacto.dto.enrollment;

import com.didacto.domain.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentQueryFilter {
    private List<Long> ids;
    @Schema(description = "WAITING, CANCELLED, ACCEPTED, REJECTED", defaultValue = "{WAITING, CANCELLED}")
    private List<EnrollmentStatus> statuses;
    private Long lectureId;
    private Long memberId;
    private Long tutorId;
}

