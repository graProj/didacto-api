package com.didacto.service.monitoring;

import com.didacto.dto.monitoring.MonitoringImageEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
@RequiredArgsConstructor
public class MonitoringImageEventHandler {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    private final RabbitTemplate rabbitTemplate;
    private Sinks.Many<MonitoringImageEvent> sink;

    /**
     * 클라이언트에서 사용될 이벤트 스트림 초기화
     */
    @PostConstruct
    private void init() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    /**
     * 큐에 쌓인 이벤트를 수신하여 스트림으로 전달
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleEvent(MonitoringImageEvent event) {
        log.debug("push event: lecture{} tutor{}", event.getLectureId(), event.getTutorId());
        sink.tryEmitNext(event);
    }

    /**
     * 큐에 이벤트 추가
     */
    public void pushEvent(MonitoringImageEvent event) {
        log.debug("push event: lecture{} tutor{}", event.getLectureId(), event.getTutorId());
        this.rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
    }

    /**
     * 스트림 반환
     */
    public Flux<MonitoringImageEvent> getStream() {
        return sink.asFlux();
    }
}