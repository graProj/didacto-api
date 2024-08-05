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
import reactor.core.publisher.FluxSink;

@Component
@Slf4j
@RequiredArgsConstructor
public class MonitoringImageEventHandler {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    private final RabbitTemplate rabbitTemplate;
    private FluxSink<MonitoringImageEvent> fluxSink;
    private Flux<MonitoringImageEvent> flux;

    /**
     * 클라이언트에서 사용될 이벤트 스트림
     */
    @PostConstruct
    private void init() {
        this.flux = Flux.create(fluxSink -> this.fluxSink = fluxSink, FluxSink.OverflowStrategy.IGNORE);
    }

    /**
     * 큐에 쌓인 이벤트를 수신하여 스트림으로 전달
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleEvent(MonitoringImageEvent event) {
        log.info("received event : {}",event.toString());
        if (this.fluxSink != null) {
            this.fluxSink.next(event);
        }
    }

    /**
     * 큐에 이벤트 추가
     */
    public void pushEvent(MonitoringImageEvent event) {
        log.info("push event: {}",event.toString());
        this.rabbitTemplate.convertAndSend(exchangeName,routingKey,event);
    }

    /**
     * 스트림 반환
     */
    public Flux<MonitoringImageEvent> getStream() {
        return this.flux;
    }
}
