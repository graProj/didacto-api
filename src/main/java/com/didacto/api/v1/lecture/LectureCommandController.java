package com.didacto.api.v1.lecture;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.domain.Lecture;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.service.lecture.LectureCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/lecture")
@Tag(name = "LECTURE-COMMAND API", description = "강의 생성 및 수정 API")
public class LectureCommandController {
    private final LectureCommandService lectureCommandService;

    @PostMapping
    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "LECTURE_COMMAND_01 : 강의 생성 (교수)", description = "강의를 생성합니다.")
    public CommonResponse<Long> create(
            @RequestBody LectureCreationRequest request
    ){
        Lecture lecture = lectureCommandService.create(request);
        return new CommonResponse(
                true, HttpStatus.OK, null, lecture.getId()
        );
    }
}
