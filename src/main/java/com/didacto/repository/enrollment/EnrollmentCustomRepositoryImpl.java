package com.didacto.repository.enrollment;


import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.LectureMember;
import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.enrollment.EnrollmentQueryConditionRequest;
import com.didacto.dto.enrollment.QEnrollmentBasicResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;

import static com.didacto.domain.QEnrollment.enrollment;
import static com.didacto.domain.QLectureMember.lectureMember;
import static com.didacto.domain.QMember.member;

@Repository
@AllArgsConstructor
public class EnrollmentCustomRepositoryImpl implements EnrollmentCustomRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public boolean existWaitingEnrollmentByMemberId(Long memberId, Long lectureId){
        Enrollment enroll =  queryFactory.selectFrom(enrollment)
                .where(enrollment.member.id.eq(memberId)
                        .and(enrollment.lecture.id.eq(lectureId))
                        .and(enrollment.status.eq(EnrollmentStatus.WAITING)))
                .fetchFirst();

        return enroll != null;
    }

    @Override
    public Enrollment findWaitingEnrollment(Long enrollId, Long memberId) {
        Enrollment enroll =  queryFactory.selectFrom(enrollment)
                .where(enrollment.id.eq(enrollId)
                        .and(enrollment.status.eq(EnrollmentStatus.WAITING)
                        .and(enrollment.member.id.eq(memberId))))
                .fetchFirst();

        return enroll;
    }

    @Override
    public Enrollment findWaitingEnrollmentByTutorId(Long enrollId, Long tutorId) {
        Enrollment enroll =  queryFactory.selectFrom(enrollment)
                .where(enrollment.id.eq(enrollId)
                        .and(enrollment.status.eq(EnrollmentStatus.WAITING)
                        .and(enrollment.lecture.owner.id.eq(tutorId))))
                .fetchFirst();

        return enroll;
    }



    @Override
    public boolean existJoinByMemberAndLecture(Long memberId, Long lectureId){
        LectureMember exist =  queryFactory.selectFrom(lectureMember)
                .where(lectureMember.member.id.eq(memberId)
                        .and(lectureMember.lecture.id.eq(lectureId))
                        .and(lectureMember.deleted.eq(false)))
                .fetchFirst();

        return exist != null;
    }

    @Override
    public List<EnrollmentBasicResponse> findEnrollmentsWithFilter(
            Long lectureId, Long memberId, EnrollmentQueryConditionRequest condition, String order)
    {
        Long page = condition.getPage();
        Long size = condition.getSize();

        long offset = (page - 1) * size; // 페이지네이션 Offset 계산

        // 동적 쿼리 : 정렬
        OrderSpecifier<?> orderSpecifier;
        if(order == null){
            orderSpecifier = enrollment.createdTime.desc();
        }
        else if (order.equals("name")) {
            orderSpecifier = enrollment.member.name.asc();
        }
        else if (order.equals("lecture")) {
            orderSpecifier = enrollment.lecture.title.asc();
        }
        else if (order.equals("email")) {
            orderSpecifier = enrollment.member.email.asc();
        }
        else {
            orderSpecifier = enrollment.createdTime.desc();
        }


        // 동적 쿼리 : 조회 조건 (학생 ID, 강의 ID)
        BooleanBuilder targetPredicate = new BooleanBuilder();
        if (lectureId != null) {
            targetPredicate.and(enrollment.lecture.id.eq(lectureId));
        }

        if (memberId != null) {
            targetPredicate.and(enrollment.member.id.eq(memberId));
        }

        // 동적 쿼리 : 상태 필터 조건 (수락, 거절, 대기, 취소)
        BooleanBuilder statusPredicate = filterStatus(condition);


        List<EnrollmentBasicResponse> enrolls = queryFactory.select(
                        new QEnrollmentBasicResponse(
                                enrollment.id, enrollment.status,
                                enrollment.lecture.id, enrollment.lecture.title,
                                enrollment.member.id,
                                enrollment.member.email, enrollment.member.name,
                                enrollment.createdTime, enrollment.modifiedTime))
                .from(enrollment)
                .leftJoin(enrollment.member, member)
                .where(targetPredicate, statusPredicate)
                .orderBy(orderSpecifier)
                .offset(offset)
                .limit(size)
                .fetch();

        return enrolls;
    }

    @Override
    public Long countEnrollmentsWithFilter(Long lectureId, Long memberId, EnrollmentQueryConditionRequest condition){

        // 동적 쿼리 : 조회 조건 (학생 ID, 강의 ID)
        BooleanBuilder targetPredicate = new BooleanBuilder();
        if (lectureId != null) {
            targetPredicate.and(enrollment.lecture.id.eq(lectureId));
        }

        if (memberId != null) {
            targetPredicate.and(enrollment.member.id.eq(memberId));
        }

        // 동적 쿼리 : 상태 필터 조건 (수락, 거절, 대기, 취소)
        BooleanBuilder statusPredicate = filterStatus(condition);

        long total = queryFactory.select(enrollment)
                .from(enrollment)
                .where(targetPredicate, statusPredicate)
                .fetchCount();

        return total;
    }


    private BooleanBuilder filterStatus(EnrollmentQueryConditionRequest condition){
        BooleanBuilder statusPredicate = new BooleanBuilder();
        if (condition.getWaiting() != null && condition.getWaiting() == true) {
            statusPredicate.or(enrollment.status.eq(EnrollmentStatus.WAITING));
        }
        if (condition.getCanceled() != null && condition.getCanceled() == true) {
            statusPredicate.or(enrollment.status.eq(EnrollmentStatus.CANCELLED));
        }
        if (condition.getAccepted() != null && condition.getAccepted() == true) {
            statusPredicate.or(enrollment.status.eq(EnrollmentStatus.ACCEPTED));
        }
        if (condition.getRejected() != null && condition.getRejected() == true) {
            statusPredicate.or(enrollment.status.eq(EnrollmentStatus.REJECTED));
        }
        return statusPredicate;
    }
}
