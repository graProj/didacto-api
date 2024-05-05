package com.didacto.service.lecturemember;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.LectureMember;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.lecturemember.LectureMemberPageResponse;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.didacto.repository.lecturemember.LectureMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LectureMemberQueryService {
    private final LectureMemberRepository lectureMemberRepository;

    public LectureMember queryOne(Long lectureMemberId){
        return lectureMemberRepository.findById(lectureMemberId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND));
    }

    public LectureMember queryOne(LectureMemberQueryFilter filter){
        return lectureMemberRepository.findLectureMember(filter)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND));
    }

    public LectureMemberPageResponse queryPage(Pageable pageable, LectureMemberQueryFilter request) {
        long page = pageable.getOffset();
        long size = pageable.getPageSize();

        // Query : 페이지네이션 및 조건 필터링
        List<LectureMember> lectureMembers = lectureMemberRepository.findLectureMemberPage(pageable, request);

        // Query : Pagenation을 위한 총 개수 집계
        long count = lectureMemberRepository.countLectureMembers(request);

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

        return new LectureMemberPageResponse(pageInfo, lectureMembers);
    }
}
