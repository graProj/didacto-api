package com.didacto.controller.v1.lecture;

import com.didacto.common.response.CommonResponse;
import com.didacto.domain.Lecture;
import com.didacto.dto.lecture.*;
import com.didacto.service.lecture.LectureQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("page")
    @Operation(summary = "LECTURE_MEMBER_QUERY_02 : 강의 목록 조회")
    public CommonResponse<LecturePageResponse> queryPage(
            @PageableDefault(size = 100)
            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @ParameterObject LectureQueryFilter request
    ){
        LecturePageResponse lecturePageResponse = lectureQueryService.queryPage(pageable, request);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 목록을 조회하였습니다.",
                lecturePageResponse
        );
    }
}
