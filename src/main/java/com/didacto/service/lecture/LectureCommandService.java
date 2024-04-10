package com.didacto.service.lecture;

import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.repository.lecture.LectureRepository;
import com.didacto.service.member.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureCommandService {
    private final LectureRepository lectureRepository;
    private final MemberQueryService memberQueryService;

    @Transactional
    public Lecture create(LectureCreationRequest request) {
        Member member = memberQueryService.query(request.getOwnerId());

        Lecture lecture = Lecture.builder()
                .title(request.getTitle())
                .owner(member)
                .deleted(false)
                .state(LectureState.WAITING)
                .build();

        return lectureRepository.save(lecture);
    }
}