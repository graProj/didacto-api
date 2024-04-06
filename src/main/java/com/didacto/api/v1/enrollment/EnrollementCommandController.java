package com.didacto.api.v1.enrollment;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Enrollment;
import com.didacto.domain.Lecture;
import com.didacto.dto.enrollment.EnrollmentBasicTypeResponse;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.service.enrollment.EnrollmentService;
import com.didacto.service.lecture.LectureQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/enrollment")
@Tag(name = "ENROLLEMENT-COMMAND API", description = "강의 구성원 등록 요청 및 처리 API") 
public class EnrollementCommandController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize(AuthConstant.AUTH_USER)
    public CommonResponse<EnrollmentBasicTypeResponse> create(
            @RequestBody EnrollmentRequest request
    ){
        Long studentId = SecurityUtil.getCurrentMemberId();

        EnrollmentBasicTypeResponse enroll = enrollmentService.requestEnrollment(request, studentId);
        return new CommonResponse(
                true, HttpStatus.OK, "교수자에게 강의 초대 요청을 보냈습니다.", enroll
        );
    }
}
