package com.didacto.repository.lecture;

import com.didacto.dto.lecture.LectureResponse;

import java.util.List;

public interface LectureCustomRepository {
    List<LectureResponse> findBoardAllByLectureNo(long LectureNo);

    void deleteByLectureIdAndMemberId(Long lectureId, Long memberId);
}
