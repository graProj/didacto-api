package com.didacto.repository.enrollment;


import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.LectureMember;
import com.didacto.dto.enrollment.EnrollmentBasicResponse;
import com.didacto.dto.enrollment.EnrollmentQueryConditionRequest;
import com.didacto.dto.enrollment.QEnrollmentBasicResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public List<EnrollmentBasicResponse> findEnrollmentsByLectureId(Long lectureId, EnrollmentQueryConditionRequest condition){
        long page = condition.getPage();
        long size = condition.getSize();
        String order = condition.getOrder();

        long offset = (page - 1) * size; // 페이지네이션 Offset 계산

        // 동적 쿼리 : 정렬
        OrderSpecifier<?> orderSpecifier;
        if (order.equals("name")) {
            orderSpecifier = enrollment.member.name.asc();
        } else {
            orderSpecifier = enrollment.createdTime.desc();
        }


        List<EnrollmentBasicResponse> enrolls = queryFactory.select(
                        new QEnrollmentBasicResponse(
                                enrollment.id, enrollment.status,
                                enrollment.lecture.id, enrollment.member.id,
                                enrollment.member.email, enrollment.member.name,
                                enrollment.createdTime, enrollment.modifiedTime))
                .from(enrollment)
                .leftJoin(enrollment.member, member)
                .where(enrollment.lecture.id.eq(lectureId), (
                        filterStatus(condition.isWaiting(), EnrollmentStatus.WAITING)
                                .or(filterStatus(condition.isCanceled(), EnrollmentStatus.CANCELLED))
                                .or(filterStatus(condition.isAccepted(), EnrollmentStatus.ACCEPTED))
                                .or(filterStatus(condition.isRejected(), EnrollmentStatus.REJECTED))
                        ))
                .orderBy(orderSpecifier)
                .offset(offset)
                .limit(size)
                .fetch();

        return enrolls;
    }

    @Override
    public Long countEnrollmentsByLectureId(Long lectureId, EnrollmentQueryConditionRequest condition){

        long total = queryFactory.select(enrollment)
                .from(enrollment)
                .where(enrollment.lecture.id.eq(lectureId), (
                        filterStatus(condition.isWaiting(), EnrollmentStatus.WAITING)
                                .or(filterStatus(condition.isCanceled(), EnrollmentStatus.CANCELLED))
                                .or(filterStatus(condition.isAccepted(), EnrollmentStatus.ACCEPTED))
                                .or(filterStatus(condition.isRejected(), EnrollmentStatus.REJECTED))
                ))
                .fetchCount();

        return total;
    }

    private BooleanExpression filterStatus(boolean include, EnrollmentStatus status){
        return include == true ? enrollment.status.eq(status) : null;
    }
}
