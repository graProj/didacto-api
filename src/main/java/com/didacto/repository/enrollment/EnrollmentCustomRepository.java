package com.didacto.repository.enrollment;

import com.didacto.domain.Enrollment;
import com.didacto.domain.Example;

import java.util.List;

public interface EnrollmentCustomRepository {

    boolean isHaveWaitingEnrollmentByMemberId(Long memberId);

    boolean alreadyJoinedCheck(Long memberId, Long lectureId);
}
