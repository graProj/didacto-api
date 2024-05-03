package com.didacto.controller.v1.lecturemember;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.LectureMember;
import com.didacto.service.lecturemember.LectureMemberCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/lecture-member")
@Tag(name = "LECTURE-MEMBER COMMAND API", description = "강의 구성원 관련 API")
public class LectureMemberCommandController {
    private final LectureMemberCommandService lectureMemberCommandService;

    @DeleteMapping("member")
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "LECTURE_MEMBER_COMMAND_01 : 강의 구성원 단일 삭제 (강의탈퇴) (학생)", description = "강의 구성원을 단일 삭제합니다.")
    public CommonResponse<Long> deleteLectureMember(
            @Parameter(example = "1")
            @RequestParam("lectureId") Long lectureId,
            @Parameter(example = "1")
            @RequestParam("memberId") Long memberId
    ){
        Long principal = SecurityUtil.getCurrentMemberId();
        LectureMember lectureMember = lectureMemberCommandService.deleteLectureMember(lectureId, memberId, principal);
        return new CommonResponse(
                true, HttpStatus.OK, null, lectureMember.getId()
        );
    }
}
