package com.didacto.service.lecture;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.MemberGradeConstant;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.exception.custom.exception.PreconditionFailException412;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Grade;
import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureModificationRequest;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.redis.RedisLockRepository;
import com.didacto.service.member.MemberQueryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.logging.Filter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureCommandService {
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;
    private final RedisLockRepository redisLockRepository;

    @Transactional
    public Lecture create(LectureCreationRequest request, Long memberId) {

        Member member = memberRepository.findByWithOptimisticLock(memberId);
//        Member member = memberQueryService.query(memberId);

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

        try {
            return lectureRepository.save(lecture);
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrencyFailureException("Lecture could not be saved due to a concurrency issue.", e);
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
