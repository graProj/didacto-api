package com.didacto.dto.enrollment;

import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.dto.member.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class EnrollmentResponse {
    public EnrollmentResponse(Enrollment enrollment){
        this.id = enrollment.getId();
        this.status = enrollment.getStatus();
        this.lecture = new LectureResponse(enrollment.getLecture());
        this.member = new MemberResponse(enrollment.getMember());
        this.createdTime = enrollment.getCreatedTime();
        this.modifiedTime = enrollment.getModifiedTime();
    }

    private Long id;

    @Schema(example = "WAITING || CANCELLED || ACCEPTED || REJECTED")

    private EnrollmentStatus status;

    private LectureResponse lecture;

    private MemberResponse member;

    private OffsetDateTime createdTime;

    private OffsetDateTime modifiedTime;

}


