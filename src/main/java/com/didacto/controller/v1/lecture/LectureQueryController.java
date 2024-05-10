package com.didacto.controller.v1.lecture;

import com.didacto.common.response.CommonResponse;
import com.didacto.domain.Lecture;
import com.didacto.dto.PageQueryRequest;
import com.didacto.dto.lecture.LecturePageResponse;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.dto.lecture.LectureQueryRequest;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.service.lecture.LectureQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/lecture")
@Tag(name = "LECTURE QUERY API", description = "강의 조회 API")
public class LectureQueryController {
    private final LectureQueryService lectureQueryService;

    @GetMapping("{lectureId}")
    @Operation(summary = "LECTURE_QUERY_01 : 강의 조회", description = "강의를 조회합니다.")
    public CommonResponse<LectureResponse> queryOne(@PathVariable("lectureId") Long lectureId) {
        Lecture lecture = lectureQueryService.queryOne(lectureId);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의를 조회하였습니다.",
                new LectureResponse(lecture)
        );
    }

    @GetMapping("list")
    @Operation(summary = "LECTURE_QUERY_02 : 강의 목록 조회")
    public CommonResponse<LecturePageResponse> queryPage(
            @ParameterObject LectureQueryRequest request
    ){
        LectureQueryFilter filter = LectureQueryFilter.builder()
                .titleKeyword(request.getTitleKeyword())
                .deleted(request.getDeleted())
                .build();
        LecturePageResponse lecturePageResponse = lectureQueryService.queryPage(request.getPageable(), filter);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 목록을 조회하였습니다.",
                lecturePageResponse
        );
    }
}
