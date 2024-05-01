package com.didacto.service.lecture;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Lecture;
import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.enrollment.EnrollmentListResponse;
import com.didacto.dto.enrollment.EnrollmentQueryConditionRequest;
import com.didacto.dto.enrollment.PageInfoResponse;
import com.didacto.dto.lecture.LectureListResponse;
import com.didacto.dto.lecture.LecturePagingRequest;
import com.didacto.repository.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureQueryService {
    private final LectureRepository lectureRepository;

    public Lecture query(Long lectureId) {

        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND));
    }


    /**
     * [키워드 검색을 통한 강의 리스트 조회]
     * 해당 강의에 해당하는 초대 정보를 조회한다.
     * @param pageInfo - 페이지네이션과 필터를 포함한 조회 조건
     * @param order - 정렬 순서
     * @param keyword - 검색 키워드
     * @return EnrollmentListResponse
     */
    public LectureListResponse queryEnrollmentListByKeyword(LecturePagingRequest pageInfo, String order, String keyword){
        long page = pageInfo.getPage();
        long size = pageInfo.getSize();

        LectureListResponse lectures = new LectureListResponse();
        return lectures;
    }
}
