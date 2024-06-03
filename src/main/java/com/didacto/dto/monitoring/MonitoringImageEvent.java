package com.didacto.dto.monitoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringImageEvent {
    private Long tutorId;
    private Long lectureId;
    private String encodedImageBase64;
}
