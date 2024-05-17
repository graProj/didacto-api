package com.didacto.controller.v1.enrollment;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonResponse;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.dto.enrollment.EnrollmentCancelRequest;
import com.didacto.dto.enrollment.EnrollmentConfirmRequest;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.service.enrollment.EnrollmentCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/enrollment")
@Tag(name = "ENROLLMENT-COMMAND API", description = "강의 구성원 등록 요청 및 처리 API")
public class EnrollmentCommandController {

    private final EnrollmentCommandService enrollmentService;

    @PostMapping
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "ENROLL_COMMAND_01 : 강의 등록 요청 (학생)", description = "해당 강의의 구성원으로 등록을 요청합니다. <br>" +
            "이미 해당 강의의 소속이거나 대기중인 해당 강의에 대한 등록 요청이 있을 시에는 보낼 수 없습니다.")
    public CommonResponse<Long> createRequest(
            @Valid @RequestBody EnrollmentRequest request
    ){
        Long studentId = SecurityUtil.getCurrentMemberId();

        Long enroll = enrollmentService.requestEnrollment(request.getLectureId(), studentId).getId();
        return new CommonResponse(
                true, HttpStatus.OK, "교수자에게 강의 등록 요청을 보냈습니다.", enroll
        );
    }

    @DeleteMapping
    @PreAuthorize(AuthConstant.AUTH_USER)
    @Operation(summary = "ENROLL_COMMAND_02 : 강의 등록 요청 취소 (학생)", description = "요청한 등록을 취소합니다.")
    public CommonResponse<Long> cancelRequest(
            @Parameter(example = "1") @RequestParam("enrollmentId") Long enrollmentId
    ){
        Long studentId = SecurityUtil.getCurrentMemberId();

        Long enroll = enrollmentService.cancelEnrollment(enrollmentId, studentId).getId();
        return new CommonResponse(
                true, HttpStatus.OK, "등록 요청이 취소되었습니다.", enroll
        );
    }

    @PutMapping
    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "ENROLL_COMMAND_03 : 강의 등록 요청 처리 (교수자)", description = "학생들이 보낸 요청을 승인/혹은 거절합니다.")
    public CommonResponse<Long> confirmRequest(
            @Valid @RequestBody EnrollmentConfirmRequest request
    ){
        Long tutorId = SecurityUtil.getCurrentMemberId();

        Enrollment enroll = enrollmentService.confirmEnrollment(
                request.getEnrollmentId(), tutorId, EnrollmentStatus.valueOf(request.getAction()));

        Long result = enroll.getId();

        //탈퇴된 User의 요청일 시 CANCEL로 상태 변경 및 예외 반환
        if(enroll.getStatus().equals(EnrollmentStatus.CANCELLED)){
            throw new NoSuchElementFoundException404(ErrorDefineCode.CONFIRM_FAIL_USER_DELETED);
        }

        else{
            return new CommonResponse(
                    true, HttpStatus.OK, "등록 요청 처리가 완료되었습니다.", result
            );
        }

    }


}
