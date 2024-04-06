package com.didacto.api.v1.enrollment;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Enrollment;
import com.didacto.domain.Lecture;
import com.didacto.dto.enrollment.EnrollmentBasicTypeResponse;
import com.didacto.dto.enrollment.EnrollmentCancelRequest;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.service.enrollment.EnrollmentService;
import com.didacto.service.lecture.LectureQueryService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "ENROLL_COMMAND_01 : 강의 참여 요청 (학생)", description = "해당 강의의 구성원으로 참여를 요청합니다. \\n" +
            "이미 해당 강의의 소속이거나 대기중인 해당 강의에 대한 참여 요청이 있을 시에는 보낼 수 없습니다.")
    public CommonResponse<EnrollmentBasicTypeResponse> createRequest(
            @RequestBody EnrollmentRequest request
    ){
        Long studentId = SecurityUtil.getCurrentMemberId();

        EnrollmentBasicTypeResponse enroll = enrollmentService.requestEnrollment(request.getLectureId(), studentId);
        return new CommonResponse(
                true, HttpStatus.OK, "교수자에게 강의 참여 요청을 보냈습니다.", enroll
        );
    }

    @DeleteMapping
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "ENROLL_COMMAND_02 : 강의 참여 요청 취소 (학생)", description = "요청한 참여를 취소합니다.")
    public CommonResponse<EnrollmentBasicTypeResponse> cancelRequest(
            @RequestBody EnrollmentCancelRequest request
    ){
        Long studentId = SecurityUtil.getCurrentMemberId();

        EnrollmentBasicTypeResponse enroll = enrollmentService.cancelEnrollment(request.getEnrollmentId(), studentId);
        return new CommonResponse(
                true, HttpStatus.OK, "참여 요청이 취소되었습니다.", enroll
        );
    }
}
