package com.didacto.repository.enrollment;

import com.didacto.domain.Enrollment;
import com.didacto.domain.Example;

import java.util.List;

public interface EnrollmentCustomRepository {

    boolean existWaitingEnrollmentByMemberId(Long memberId, Long lectureId);

    Enrollment findWaitingEnrollment(Long enrollId, Long memberId);

    Enrollment findWaitingEnrollmentByTutorId(Long enrollId, Long memberId);

//    boolean is(Long memberId);

    boolean existJoinByMemberAndLecture(Long memberId, Long lectureId);
}