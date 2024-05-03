package com.didacto.repository.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LectureMemberCustomRepository {
    Optional<LectureMember> findLectureMember(LectureMemberQueryFilter filter);
    List<LectureMember> findLectureMembers(LectureMemberQueryFilter filter);
    List<LectureMember> findLectureMemberPage(Pageable pageable, LectureMemberQueryFilter request);
    Long countLectureMembers(LectureMemberQueryFilter request);
}
