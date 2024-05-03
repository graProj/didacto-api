package com.didacto.service.lecturemember;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.repository.lecture.LectureCustomRepository;
import com.didacto.repository.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class LectureMemberQueryService {

    private final LectureRepository lectureRepository;

    public void deleteLectureMember(Long lectureId, Long memberId) {
        lectureRepository.deleteByLectureIdAndMemberId(lectureId, memberId);
        if(memberId == null){
            throw new NoSuchElementFoundException404(ErrorDefineCode.MEMBER_NOT_FOUND);
        } else if (lectureId == null) {
            throw new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND);
        }
    }
}