package com.didacto.api.v1.enrollment;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/enrollment")
@Tag(name = "ENROLLEMENT-QUERY API", description = "강의 구성원 등록 요청 및 처리 API")
public class EnrollementQueryController {

    private final EnrollmentQueryService enrollmentQueryService;

    @GetMapping("/{lectureId}")
    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "ENROLL_QUERY_01 : 강의 참여 요청 목록 조회 (교수자)", description = "해당 강의의 참여 요청 목록들을 조회합니다.")
    public CommonResponse<EnrollmentListResponse> queryEnrollmentsByLecture(
            @Schema(description = "강의 PK", example = "1")
            @PathVariable Long lectureId,

            // 페이지네이션, 검색 필터 정보 포함
            @ParameterObject EnrollmentQueryConditionRequest request

    ){
        Long tutorId = SecurityUtil.getCurrentMemberId();

        //TODO : 해당 교수자가 해당 강의의 Owner가 맞는지 확인하는 로직 추가

        EnrollmentListResponse response = enrollmentQueryService.getEnrollmentInfoByLecture(lectureId, request);
        return new CommonResponse(
                true, HttpStatus.OK, "참여 목록을 조회하였습니다", response
        );

    }


}
