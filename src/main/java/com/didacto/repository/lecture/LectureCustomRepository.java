package com.didacto.repository.lecture;

import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import com.didacto.dto.lecture.LectureQueryFilter;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureCustomRepository {


    List<Lecture> findLecturePage(Pageable pageable, LectureQueryFilter request);

    Long countLectures(LectureQueryFilter request);

    Long countLecturesExceptdeleted(LectureQueryFilter request);

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    //@Query("select count(s) from Lecture s where s.lecture = :lecture")
    //Long countLecturesByOwner(Lecture lecture);



}
