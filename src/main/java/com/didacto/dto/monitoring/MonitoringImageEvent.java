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
    private SSEType type;
    private Long tutorId;
    private Long lectureId;
    private String encodedImageBase64;

    static public MonitoringImageEvent createInitEvent() {
        return MonitoringImageEvent.builder()
                .type(SSEType.INIT)
                .build();
    }
}
