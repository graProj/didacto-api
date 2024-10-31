package com.didacto.controller.v1.lecturemember;


import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.LectureMember;
import com.didacto.dto.lecturemember.LectureMemberPageResponse;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.didacto.dto.lecturemember.LectureMemberQueryRequest;
import com.didacto.dto.lecturemember.LectureMemberResponse;
import com.didacto.service.lecturemember.LectureMemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/lecture-member")
@Tag(name = "LECTURE-MEMBER QUERY API", description = "강의 구성원 조회 API")
public class LectureMemberQueryController {
    private final LectureMemberQueryService lectureMemberQueryService;

    @GetMapping("{lectureMemberId}")
    @PreAuthorize(AuthConstant.AUTH_ALL)
    @Operation(summary = "LECTURE_MEMBER_QUERY_01 : 강의 구성원 조회")
    public CommonResponse<LectureMemberResponse> queryOne(@PathVariable("lectureMemberId") Long lectureId) {
        LectureMember lectureMember = lectureMemberQueryService.queryOne(lectureId);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 구성원을 조회하였습니다.",
                new LectureMemberResponse(lectureMember)
        );
    }

    @GetMapping("list/member")
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "LECTURE_MEMBER_QUERY_02 : 유저가 속한 강의 목록 조회 (학생)")
    public CommonResponse<LectureMemberPageResponse> queryByMember(
            @ParameterObject LectureMemberQueryRequest request
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();
        LectureMemberQueryFilter filter = LectureMemberQueryFilter.builder()
                .memberId(memberId)
                .deleted(request.getDeleted())
                .build();
        LectureMemberPageResponse lectureMemberPageResponse = lectureMemberQueryService.queryPage(request.getPageable(), filter);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 구성 목록을 조회하였습니다.",
                lectureMemberPageResponse
        );
    }

    @GetMapping("list/lecture/{lectureId}")
    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "LECTURE_MEMBER_QUERY_03 : 강의에 속한 학생 목록 조회 (교수자)")
    public CommonResponse<LectureMemberPageResponse> queryByLecture(
            @PathVariable("lectureId") @Schema(example = "1") Long lectureId,
            @ParameterObject LectureMemberQueryRequest request
    ){
        LectureMemberQueryFilter filter = LectureMemberQueryFilter.builder()
                .lectureId(lectureId)
                .deleted(request.getDeleted())
                .build();
        LectureMemberPageResponse lectureMemberPageResponse = lectureMemberQueryService.queryPage(request.getPageable(), filter);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 구성 목록을 조회하였습니다.",
                lectureMemberPageResponse
        );
    }
}
