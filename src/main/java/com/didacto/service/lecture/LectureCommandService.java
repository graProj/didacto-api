package com.didacto.service.lecture;

import com.didacto.domain.Lecture;
import com.didacto.domain.LectureState;
import com.didacto.dto.lecture.LectureCreationRequest;
import com.didacto.repository.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureCommandService {
    private final LectureRepository lectureRepository;

    @Transactional
    public Lecture create(LectureCreationRequest request) {
        Lecture lecture = Lecture.builder()
                .title(request.getTitle())
//                .ownerId(request.getOwnerId())
                .deleted(false)
                .state(LectureState.WAITING)
                .build();

        return lectureRepository.save(lecture);
    }
}