package com.didacto.repository.lecture;

import com.didacto.domain.Lecture;
import com.didacto.dto.lecture.LectureQueryFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LectureCustomRepository {
    List<Lecture> findLecturePage(Pageable pageable, LectureQueryFilter request);
    Long countLectures(LectureQueryFilter request);
}
