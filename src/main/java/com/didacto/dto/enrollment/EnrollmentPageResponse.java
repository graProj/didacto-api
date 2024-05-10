package com.didacto.dto.enrollment;

import com.didacto.domain.Enrollment;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentPageResponse extends PageResponse {
    private List<EnrollmentResponse> enrollments;

    public EnrollmentPageResponse(
            PageInfoResponse pageInfo,
            List<Enrollment> enrollments
    ){
        super(pageInfo);
        this.enrollments = enrollments.stream()
                .map(EnrollmentResponse::new)
                .toList();
    }
}