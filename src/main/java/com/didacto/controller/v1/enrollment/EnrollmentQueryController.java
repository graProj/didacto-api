package com.didacto.controller.v1.enrollment;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.enrollment.*;
import com.didacto.service.enrollment.EnrollmentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/enrollment")
@Tag(name = "ENROLLMENT-QUERY API", description = "강의 구성원 등록 요청 및 처리 API")
public class EnrollmentQueryController {

    private final EnrollmentQueryService enrollmentQueryService;

    @GetMapping("/{enrollId}")
    @PreAuthorize(AuthConstant.AUTH_ALL)
    @Operation(summary = "ENROLL_QUERY_01 : PK로 강의 등록 요청 데이터 조회 (공통)", description = "해당 PK에 해당하는 등록 요청 데이터 조회")
    public CommonResponse<EnrollmentBasicResponse> queryEnrollmentById(
            @Schema(description = "초대정보 PK", example = "1")
            @PathVariable Long enrollId
    ){
        EnrollmentBasicResponse response = enrollmentQueryService.getEnrollmentById(enrollId);
        return new CommonResponse(
                true, HttpStatus.OK, "강의 등록 요청 데이터를 조회하였습니다", response
        );
    }

    @GetMapping()
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "ENROLL_QUERY_02 : 강의 등록 요청 목록 조회 (학생)", description = "해당 학생이 요청한 등록 요청 목록들을 조회합니다.<br>" +
            "페이지네이션, 정렬 기준, 등록 요청 상태(WAITING/ACCEPTED/REJECTED/CANCELED)에 대해 필터링(모두 필터링하지 않을 시 전체조회)하여 조회합니다.<br>" +
            "규칙은 ENROLL_QUERY_01과 같습니다.")
    public CommonResponse<EnrollmentListResponse> queryEnrollmentsByUser(

            @RequestParam(required = false)
            @Parameter(description = "정렬 기준(date/lecture)(생성일, 강의명)", example = "date")
            String order,

            // 페이지네이션, 검색 필터 정보 포함
            @ParameterObject EnrollmentQueryConditionRequest request

    ){
        Long studentId = SecurityUtil.getCurrentMemberId();

        EnrollmentListResponse response = enrollmentQueryService.getEnrollmentInfoList(null, studentId, request, order);
        return new CommonResponse(
                true, HttpStatus.OK, "강의 등록 요청 목록을 조회하였습니다", response
        );
    }

    @GetMapping("/lecture/{lectureId}")
    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "ENROLL_QUERY_03 : 강의 등록 요청 목록 조회 (교수자)", description = "해당 강의의 등록 요청 목록들을 조회합니다.<br>" +
            "페이지네이션, 정렬 기준, 등록 요청 상태(WAITING/ACCEPTED/REJECTED/CANCELED)에 대해 필터링(모두 필터링하지 않을 시 전체조회)하여 조회합니다.<br>" +
            "- http://localhost:8080/api/v1/enrollment/1?page=1&size=10&order=date : 1페이지를 10개 단위로 날짜 순으로 정렬하여 모든 상태 조회<br>" +
            "- http://localhost:8080/api/v1/enrollment/1?page=1&size=10&order=date&waiting=true : 위에서 waiting 상태만 조회<br>" +
            "- http://localhost:8080/api/v1/enrollment/1?page=1&size=10&order=date&waiting=true&canceled=true : 위에서 canceled 상태까지 함께 조회<br>"
    )
    public CommonResponse<EnrollmentListResponse> queryEnrollmentsByLecture(
            @Schema(description = "강의 PK", example = "1")
            @PathVariable Long lectureId,

            @RequestParam(required = false)
            @Parameter(description = "정렬 기준(date/email/name)(생성일, 요청자 이메일, 요청자 이름)", example = "date")
            String order,

            // 페이지네이션, 검색 필터 정보 포함
            @ParameterObject EnrollmentQueryConditionRequest request

    ){
        //TODO : 해당 교수자가 해당 강의의 Owner가 맞는지 확인하는 로직 추가
        Long tutorId = SecurityUtil.getCurrentMemberId();

        EnrollmentListResponse response = enrollmentQueryService.getEnrollmentInfoList(lectureId, null, request, order);
        return new CommonResponse(
                true, HttpStatus.OK, "강의 등록 요청 목록을 조회하였습니다", response
        );
    }






}
