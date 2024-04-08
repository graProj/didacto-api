package com.didacto.api.v1.lecture;

import com.didacto.domain.Lecture;
import com.didacto.dto.lecture.LectureResponse;
import com.didacto.service.lecture.LectureQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/lecture/query")
public class LectureQueryController {
    private final LectureQueryService lectureQueryService;

    @GetMapping("{lectureId}")
    public LectureResponse query(@PathVariable("lectureId") Long lectureId) {
        Lecture lecture = lectureQueryService.query(lectureId);

        return new LectureResponse(lecture);
    }
}
