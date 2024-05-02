package com.didacto.repository.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;

import java.util.List;

public interface LectureMemberCustomRepository {
    List<LectureMember> findLectureMembersWithFilter(LectureMemberQueryFilter filter);
}
