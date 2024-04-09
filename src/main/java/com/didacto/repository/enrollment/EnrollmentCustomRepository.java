package com.didacto.repository.enrollment;

import com.didacto.domain.Enrollment;
import com.didacto.domain.Example;
import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.enrollment.EnrollmentQueryConditionRequest;

import java.util.List;

public interface EnrollmentCustomRepository {

    boolean existWaitingEnrollmentByMemberId(Long memberId, Long lectureId);

    Enrollment findWaitingEnrollment(Long enrollId, Long memberId);

    Enrollment findWaitingEnrollmentByTutorId(Long enrollId, Long memberId);

    boolean existJoinByMemberAndLecture(Long memberId, Long lectureId);

    List<EnrollmentBasicResponse> findEnrollmentsByLectureId(Long lectureId, EnrollmentQueryConditionRequest condition);

    Long countEnrollmentsByLectureId(Long lectureId, EnrollmentQueryConditionRequest condition);
}
