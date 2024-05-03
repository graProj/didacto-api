package com.didacto.repository.lecture;

import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.lecture.LectureResponse;

import java.util.List;

public interface LectureCustomRepository {

    /**
     * 강의 리스트 조회 (with 키워드, 페이지네이션)
     * @param order - 정렬 순서(date, title)
     * @param keyword - 검색 키워드(없을 시 전체조회)
     * @param page - 현재 페이지
     * @param size - 페이지 사이즈
     * @return LectureResponse
     */
    List<LectureResponse> findLecturesByKeyword(String order, String keyword, Long page, Long size);

    /**
     * 강의 페이지네이션 정보 조회
     * @param keyword - 검색 키워드(없을 시 전체)
     */
    Long countLecturesByKeyword(String keyword);
}
