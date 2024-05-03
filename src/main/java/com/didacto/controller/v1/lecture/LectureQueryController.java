package com.didacto.controller.v1.lecture;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.domain.Lecture;
import com.didacto.dto.enrollment.EnrollmentListResponse;
import com.didacto.dto.lecture.LectureListResponse;
import com.didacto.dto.lecture.LecturePagingRequest;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.service.lecture.LectureQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/lecture/query")
@Tag(name = "LECTURE-QUERY API", description = "강의 조회 API")
public class LectureQueryController {
    private final LectureQueryService lectureQueryService;

    @GetMapping("{lectureId}")
    @Operation(summary = "LECTURE_QUERY_01 : 강의 조회", description = "강의를 조회합니다.")
    public LectureResponse query(@PathVariable("lectureId") Long lectureId) {
        Lecture lecture = lectureQueryService.query(lectureId);

        return new LectureResponse(lecture);
    }


    @GetMapping("")
    @PreAuthorize(AuthConstant.AUTH_ALL)
    @Operation(summary = "ENROLL_QUERY_02 : 강의 리스트 조회 (+키워드 검색)", description = "키워드가 포함된 이름의 강의 리스트를 검색합니다.<br>" +
            "페이지네이션 정보와 검색 키워드, 정렬 순서를 파라미터로 받습니다."
    )
    public CommonResponse<EnrollmentListResponse> queryListByKeyword(

            @RequestParam(required = false)
            @Parameter(description = "검색 키워드(없을 시 전체조회)", example = "SMU")
            String keyword,

            @RequestParam(required = false)
            @Parameter(description = "정렬 기준(date/title)(생성일, 강의명), 디폴트값 date", example = "date")
            String order,

            @ParameterObject LecturePagingRequest page

    ){
        LectureListResponse response = lectureQueryService.queryEnrollmentListByKeyword(page, order, keyword);
        return new CommonResponse(
                true, HttpStatus.OK, "강의 목록을 조회하였습니다", response
        );
    }
}
