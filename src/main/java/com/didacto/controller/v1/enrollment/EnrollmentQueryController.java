package com.didacto.controller.v1.enrollment;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.enrollment.*;
import com.didacto.service.enrollment.EnrollmentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/enrollment")
@Tag(name = "ENROLLMENT-QUERY API", description = "강의 구성원 등록 요청 및 처리 API")
public class EnrollmentQueryController {

    private final EnrollmentQueryService enrollmentQueryService;

    @GetMapping("{enrollmentId}")
    @PreAuthorize(AuthConstant.AUTH_ALL)
    @Operation(summary = "ENROLL_QUERY_01 : PK로 강의 등록 요청 데이터 조회 (공통)", description = "해당 PK에 해당하는 등록 요청 데이터 조회")
    public CommonResponse<EnrollmentResponse> queryEnrollmentById(
            @Schema(example = "1")
            @PathVariable("enrollmentId") Long enrollmentId
    ){
        EnrollmentResponse response = enrollmentQueryService.getEnrollmentById(enrollmentId);
        return new CommonResponse(
                true, HttpStatus.OK, "강의 등록 요청 데이터를 조회하였습니다", response
        );
    }

    @GetMapping("list/member")
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "ENROLL_QUERY_02 : 강의 등록 요청 목록 조회 (학생)", description = "해당 학생이 요청한 등록 요청 목록들을 조회합니다")
    public CommonResponse<EnrollmentPageResponse> queryEnrollmentsByUser(
            @ParameterObject EnrollmentQueryRequest request

    ){
        Long studentId = SecurityUtil.getCurrentMemberId();

        EnrollmentQueryFilter filter = EnrollmentQueryFilter.builder()
                .memberId(studentId)
                .statuses(request.getStatuses())
                .build();

        EnrollmentPageResponse response = enrollmentQueryService.queryPage(request.getPageable(), filter);
        return new CommonResponse(
                true, HttpStatus.OK, "강의 등록 요청 목록을 조회하였습니다", response
        );
    }

    @GetMapping("list/lecture/{lectureId}")
    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "ENROLL_QUERY_03 : 강의 등록 요청 목록 조회 (교수자)", description = "해당 강의의 등록 요청 목록을 조회합니다.")
    public CommonResponse<EnrollmentPageResponse> queryEnrollmentsByLecture(
            @Schema(example = "1")
            @PathVariable("lectureId") Long lectureId,
            @ParameterObject EnrollmentQueryRequest request
    ){
        //TODO : 해당 교수자가 해당 강의의 Owner가 맞는지 확인하는 로직 추가
        Long tutorId = SecurityUtil.getCurrentMemberId();

        EnrollmentQueryFilter filter = EnrollmentQueryFilter.builder()
                .lectureId(lectureId)
                .statuses(request.getStatuses())
                .build();

        EnrollmentPageResponse response = enrollmentQueryService.queryPage(request.getPageable(), filter);
        return new CommonResponse(
                true, HttpStatus.OK, "강의 등록 요청 목록을 조회하였습니다", response
        );
    }
}
