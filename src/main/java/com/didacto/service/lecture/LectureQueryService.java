package com.didacto.service.lecture;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Lecture;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.lecture.*;
import com.didacto.repository.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureQueryService {
    private final LectureRepository lectureRepository;

    public Lecture queryOne(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND));
    }

    public LecturePageResponse queryPage(Pageable pageable, LectureQueryFilter request) {
        long page = pageable.getOffset();
        long size = pageable.getPageSize();

        // Query : 페이지네이션 및 조건 필터링
        List<Lecture> lectures = lectureRepository.findLecturePage(pageable, request);

        // Query : Pagenation을 위한 총 개수 집계
        long count = lectureRepository.countLectures(request);

        // Calc : 총 페이지 수와 다음 페이지 존재 여부 계산
        long totalPage = (long) Math.ceil((double) count / size);
        boolean isHaveNext = page < totalPage;

        // Out
        PageInfoResponse pageInfo = PageInfoResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(totalPage)
                .totalElements(count)
                .haveNext(isHaveNext)
                .build();

        return new LecturePageResponse(pageInfo, lectures);
    }
}
