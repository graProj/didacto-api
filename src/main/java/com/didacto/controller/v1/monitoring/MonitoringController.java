package com.didacto.controller.v1.monitoring;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.monitoring.MonitoringImageEvent;
import com.didacto.dto.monitoring.MonitoringImageUploadRequest;
import com.didacto.service.monitoring.MonitoringImageEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/monitoring")
@Tag(name = "MONITORING API", description = "모니터링 관련 API")
public class MonitoringController {
    private final MonitoringImageEventService monitoringService;

    @PostMapping("image/upload")
    @Operation(summary = "MONITORING_01 : 모니터링(사용자 화면) 이미지 업로드")
    @PreAuthorize(AuthConstant.AUTH_USER)
    public CommonResponse uploadImage(
            @RequestBody MonitoringImageUploadRequest request
    ) {
        monitoringService.pushEvent(MonitoringImageEvent.builder()
                .tutorId(SecurityUtil.getCurrentMemberId())
                .lectureId(request.getLectureId())
                .encodedImageBase64(request.getEncodedImageBase64())
                .build()
        );

        return new CommonResponse(
                true, HttpStatus.OK, null, null
        );
    }

    @GetMapping(value = "image-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    @PreAuthorize(AuthConstant.AUTH_ADMIN) 테스트를 위한 주석
    @Operation(summary = "MONITORING_02 : 모니터링(학생 화면) 이미지 스트림 연결",
            description = "SSE 방식으로 이미지 스트림을 연결합니다. 스트림에선 모니터링 이미지를 포함한 정보를 방출합니다." +
                    "<br>swagger-ui에서는 확인이 어렵습니다. Postman 등을 이용해 확인해주세요.")
    public Flux<MonitoringImageEvent> imageStream(
            @RequestParam("lectureId") Long lectureId
    ) {
        return monitoringService.getStream()
                .filter(event -> event.getLectureId().equals(lectureId));
    }
}
