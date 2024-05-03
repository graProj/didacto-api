package com.didacto.repository.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LectureMemberCustomRepository {
    List<LectureMember> findLectureMembersWithFilter(LectureMemberQueryFilter filter);
    List<LectureMember> findLectureMemberPage(Pageable pageable, LectureMemberQueryFilter request);
    long countLectureMemberPage(LectureMemberQueryFilter request);
}
