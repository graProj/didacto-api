package com.didacto.service.lecturemember;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.LectureMember;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.didacto.repository.lecturemember.LectureMemberCustomRepository;
import com.didacto.repository.lecturemember.LectureMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LectureMemberQueryService {
    private final LectureMemberRepository lectureMemberRepository;
    private final LectureMemberCustomRepository lectureMemberCustomRepository;

    public LectureMember query(Long lectureMemberId){
        return lectureMemberRepository.findById(lectureMemberId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.LECTURE_NOT_FOUND));
    }

    public List<LectureMember> queryWithFilter(LectureMemberQueryFilter filter){
        return lectureMemberCustomRepository.findLectureMembersWithFilter(filter);
    }
}
