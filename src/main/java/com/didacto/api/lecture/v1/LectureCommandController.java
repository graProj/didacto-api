package com.didacto.api.lecture.v1;

import com.didacto.common.response.CommonResponse;
import com.didacto.domain.Lecture;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.service.lecture.LectureCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/lecture")
public class LectureCommandController {
    private final LectureCommandService lectureCommandService;

    @PostMapping
    public CommonResponse<Lecture> create(
            @RequestBody LectureCreationRequest request
    ){
        Lecture lecture = lectureCommandService.create(request);
        return new CommonResponse<Lecture>(
                true, HttpStatus.OK, null, lecture
        );
    }
}
