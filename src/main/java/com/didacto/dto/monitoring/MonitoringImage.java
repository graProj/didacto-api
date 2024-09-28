package com.didacto.dto.monitoring;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonitoringImage {
    private Long lectureId;
    private Long memberId;
    private String encodedImageBase64;
}
