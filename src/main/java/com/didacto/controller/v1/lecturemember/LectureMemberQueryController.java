package com.didacto.controller.v1.lecturemember;


import com.didacto.common.response.CommonResponse;
import com.didacto.domain.LectureMember;
import com.didacto.dto.PageQueryRequest;
import com.didacto.dto.lecturemember.LectureMemberPageResponse;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.didacto.dto.lecturemember.LectureMemberResponse;
import com.didacto.service.lecturemember.LectureMemberQueryService;
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
@RequestMapping("api/v1/lecture-member")
@Tag(name = "LECTURE-MEMBER QUERY API", description = "강의 구성원 조회 API")
public class LectureMemberQueryController {
    private final LectureMemberQueryService lectureMemberQueryService;

    @GetMapping("{lectureMemberId}")
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

    @GetMapping("page")
    @Operation(summary = "LECTURE_MEMBER_QUERY_02 : 강의 구성원 목록 조회")
    public CommonResponse<LectureMemberPageResponse> queryPage(
            @ParameterObject PageQueryRequest pageable,
            @ParameterObject LectureMemberQueryFilter request
    ){
        LectureMemberPageResponse lectureMemberPageResponse = lectureMemberQueryService.queryPage(pageable.toPageable(), request);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "강의 구성 목록을 조회하였습니다.",
                lectureMemberPageResponse
        );
    }
}
