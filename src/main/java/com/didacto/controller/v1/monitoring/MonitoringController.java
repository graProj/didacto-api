package com.didacto.controller.v1.monitoring;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.monitoring.MonitoringImageEvent;
import com.didacto.dto.monitoring.MonitoringImageUploadRequest;
import com.didacto.dto.monitoring.SSEType;
import com.didacto.service.monitoring.MonitoringImageEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("image/upload")
    @Operation(summary = "MONITORING_01 : 모니터링(사용자 화면) 이미지 업로드")
    @PreAuthorize(AuthConstant.AUTH_USER)
    public CommonResponse uploadImage(
            @RequestBody MonitoringImageUploadRequest request
    ) {
        monitoringService.pushEvent(MonitoringImageEvent.builder()
                .type(SSEType.DATA)
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
    public Flux<MonitoringImageEvent> imageStream(
            @RequestParam("lectureId") Long lectureId
    ) {
        // 초기 연결 신호
        Flux<MonitoringImageEvent> initEvent = Flux.just(MonitoringImageEvent.createInitEvent());

        // 실제 데이터
        Flux<MonitoringImageEvent> monitoringStream = monitoringService.stream(lectureId);

        return Flux.concat(initEvent, monitoringStream)
                .doOnSubscribe(subscription -> logger.info("Stream subscribed for lectureId: " + lectureId))
                .doOnCancel(() -> logger.info("Stream cancelled for lectureId: " + lectureId));
    }
}
