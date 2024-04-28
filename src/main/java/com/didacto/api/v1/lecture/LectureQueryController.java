package com.didacto.api.v1.lecture;

import com.didacto.domain.Lecture;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.service.lecture.LectureQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
