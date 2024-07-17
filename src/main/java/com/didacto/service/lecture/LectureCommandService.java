package com.didacto.service.lecture;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.exception.custom.exception.PreconditionFailException412;
import com.didacto.domain.Grade;
import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.dto.lecture.LectureModificationRequest;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.repository.member.MemberRepository;
import com.didacto.service.member.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureCommandService {
    private final LectureRepository lectureRepository;
    private final MemberQueryService memberQueryService;
    private final MemberRepository memberRepository;

    @Transactional
    public Lecture create(LectureCreationRequest request, Long createdBy) {
        Member member = memberQueryService.query(createdBy);

        if (member.getGrade() == Grade.Premium && member.getGradeExpiration().isBefore(OffsetDateTime.now().now())) {
            member.downgradeToFreeTeer();
            memberRepository.save(member);
        }

        long lectureCount = lectureRepository.countByOwner(member);
        if (member.getGrade() == Grade.Freeteer && lectureCount >= 3) {
            throw new PreconditionFailException412(ErrorDefineCode.LECTURE_MEMBER_FREETEER_OVERCOUNT_3);
        }


        Lecture lecture = Lecture.builder()
                .title(request.getTitle())
                .owner(member)
                .state(LectureState.WAITING)
                .build();

        return lectureRepository.save(lecture);
    }

    public Lecture modify(LectureModificationRequest request) {
        Lecture lecture = lectureRepository.findById(request.getLectureId()).orElseThrow(()
                -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND)
        );

        lecture.modify(request.getTitle());

        return lectureRepository.save(lecture);
    }

    public Lecture delete(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(()
                -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND)
        );

        lecture.delete();

        return lectureRepository.save(lecture);
    }
}