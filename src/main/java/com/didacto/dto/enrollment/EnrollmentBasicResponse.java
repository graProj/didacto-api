package com.didacto.dto.enrollment;

import com.didacto.domain.EnrollmentStatus;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Builder
@Schema(title = "Enrollment : 기본 데이터 Response")
public class EnrollmentBasicResponse {

    @QueryProjection
    public EnrollmentBasicResponse(Long id, EnrollmentStatus status, Long lecture_id, String lecture_name, Long member_id, String member_email, String member_name, OffsetDateTime createdTime, OffsetDateTime modifiedTime) {
        this.id = id;
        this.status = status;
        this.lecture_id = lecture_id;
        this.lecture_name = lecture_name;
        this.member_id = member_id;
        this.member_email = member_email;
        this.member_name = member_name;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    @Schema(example = "1")
    private Long id;

    @Schema(example = "WAITING || CANCELLED || ACCEPTED || REJECTED")
    private EnrollmentStatus status;

    @Schema(example = "1")
    private Long lecture_id;

    @Schema(example = "자료구조")
    private String lecture_name;

    @Schema(example = "1")
    private Long member_id;

    @Schema(example = "testtest@gmail.com")
    private String member_email;

    @Schema(example = "홍길동")
    private String member_name;

    private OffsetDateTime createdTime;

    private OffsetDateTime modifiedTime;

}


