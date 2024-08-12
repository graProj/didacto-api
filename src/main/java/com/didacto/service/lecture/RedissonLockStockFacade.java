package com.didacto.service.lecture;

import com.didacto.dto.lecture.LectureCreationRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;
    private final LectureCommandService lectureCommandService;

    public void create(LectureCreationRequest request, Long memberId) {
        RLock lock = redissonClient.getLock(memberId.toString());

        try {
            boolean acquireLock = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!acquireLock) {
                System.out.println("Lock 획득 실패");
                return;
            }
            lectureCommandService.create(request, memberId);
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }
}