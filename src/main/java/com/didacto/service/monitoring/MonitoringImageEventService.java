package com.didacto.service.monitoring;

import com.didacto.dto.monitoring.MonitoringImageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonitoringImageEventService {
    private final MonitoringImageEventHandler monitoringImageEventHandler;

    /**
     * 모니터링 이미지 스트림 연결
     */
    public Flux<MonitoringImageEvent> getStream() {
        return monitoringImageEventHandler.getStream();
    }

    /**
     * 모니터링(사용자 화면 이미지) 전송
     */
    public void pushEvent(MonitoringImageEvent event) {
        monitoringImageEventHandler.pushEvent(event);
    }
}