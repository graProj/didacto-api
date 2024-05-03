package com.didacto.dto.enrollment;


import com.didacto.dto.PageInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "Enrollment : 데이터 리스트 응답")
public class EnrollmentListResponse {

    private List<EnrollmentBasicResponse> enrollments;

    private PageInfoResponse pageInfo;
}
