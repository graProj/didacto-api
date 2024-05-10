package com.didacto.dto.enrollment;

import com.didacto.domain.EnrollmentStatus;
import com.didacto.dto.PageQueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQueryRequest extends PageQueryRequest {
    @Schema(description = "상태: WAITING, CANCELLED, ACCEPTED, REJECTED", example = "WAITING, ACCEPTED")
    private List<EnrollmentStatus> statuses;
}
