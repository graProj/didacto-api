package com.didacto.repository.enrollment;

import com.didacto.domain.Enrollment;
import com.didacto.domain.Example;
import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.enrollment.EnrollmentQueryConditionRequest;

import java.util.List;

public interface EnrollmentCustomRepository {

    /**
     * User가 해당 Lecture에 초대 신청을 하여 대기중(Waiting)인 Enrollment 존재 여부 조회
     */
    boolean existWaitingEnrollmentByMemberId(Long memberId, Long lectureId);

    /**
     * 대기중(Waiting)이고, 해당 member의 소유인 Enrollment 조회
     */
    Enrollment findWaitingEnrollment(Long enrollId, Long memberId);

    /**
     * 대기중(Waiting)이고, 해당 tutor의 처리 권한이 있는 Enrollment 조회
     */
    Enrollment findWaitingEnrollmentByTutorId(Long enrollId, Long tutorId);


    /**
     * 이미 해당 Member가 Lecture에 소속되어 있는 지 조회
     */
    boolean existJoinByMemberAndLecture(Long memberId, Long lectureId);


    /**
     * 초대 요청 리스트 조회 (with 필터, 페이지네이션)
     * @param lectureId - 해당 강의에 들어온 초대에 대해서 필터링 (null일 시 전체)
     * @param memberId - 해당 멤버가 요청한 것에 대해서 필터링 (null일 시 전체)
     * @param condition - 조건 (페이지네이션, 초대 상태 필터)
     * @return
     */
    List<EnrollmentBasicResponse> findEnrollmentsWithFilter(Long lectureId, Long memberId, EnrollmentQueryConditionRequest condition, String order);


    /**
     * 초대 요청 총 개수 조회 (with 필터, 페이지네이션)
     * @param lectureId - 해당 강의에 들어온 초대에 대해서 필터링 (null일 시 전체)
     * @param memberId - 해당 멤버가 요청한 것에 대해서 필터링 (null일 시 전체)
     * @param condition - 조건 (페이지네이션, 초대 상태 필터)
     * @return
     */
    Long countEnrollmentsWithFilter(Long lectureId, Long memberId, EnrollmentQueryConditionRequest condition);



}
