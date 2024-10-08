package com.didacto.service.lecture;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.MemberGradeConstant;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.exception.custom.exception.PreconditionFailException412;
import com.didacto.config.exception.custom.exception.TimeOutException408;
import com.didacto.domain.Grade;
import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureModificationRequest;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.service.member.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureCommandService {
    private final LectureRepository lectureRepository;
    private final MemberQueryService memberQueryService;
    private final RedissonClient redissonClient;

    @Transactional
    public Lecture create(LectureCreationRequest request, Long memberId) {

        // Redisson 락 획득
        RLock lock = redissonClient.getLock(memberId.toString());

        try {
            boolean acquireLock = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!acquireLock) {
                throw new PreconditionFailException412(ErrorDefineCode.LECTURE_CONCURRENCY_FAIL_GET_LOCK);
            }

            Member member = memberQueryService.query(memberId);

            // Freeteer이고, 최대 강의 개수를 초과하면 예외 반환
            if (member.getGrade() == Grade.Freeteer) {
                LectureQueryFilter filter = LectureQueryFilter.builder()
                        .owner(member)
                        .deleted(false)
                        .build();

                long lectureCount = lectureRepository.countLectures(filter);

                if (lectureCount >= MemberGradeConstant.MAX_LECTURES) {
                    throw new PreconditionFailException412(ErrorDefineCode.LECTURE_MEMBER_FREETEER_OVERCOUNT_3);
                }
            }

            // Lecture 엔티티 생성
            Lecture lecture = Lecture.builder()
                    .title(request.getTitle())
                    .owner(member)
                    .state(LectureState.WAITING)
                    .build();

            return lectureRepository.save(lecture);

        } catch (InterruptedException e) {
            // 예외 처리 로직
            throw new TimeOutException408(ErrorDefineCode.LECTURE_CONCURRENCY_INTERRUPT);
        } finally {
            lock.unlock();  // 락 해제
        }
    }



    @Transactional
    public Lecture modify(LectureModificationRequest request) {
        Lecture lecture = lectureRepository.findById(request.getLectureId()).orElseThrow(()
                -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND)
        );

        lecture.modify(request.getTitle());

        return lectureRepository.save(lecture);
    }

    @Transactional
    public Lecture delete(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(()
                -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND)
        );

        lecture.delete();

        return lectureRepository.save(lecture);
    }

}
