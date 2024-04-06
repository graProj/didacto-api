package com.didacto.repository.enrollment;


import com.didacto.domain.Enrollment;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.domain.LectureMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;


import static com.didacto.domain.QEnrollment.enrollment;
import static com.didacto.domain.QLectureMember.lectureMember;

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
    public Enrollment findWaitingEnrollmentById(Long enrollId, Long memberId) {
        Enrollment enroll =  queryFactory.selectFrom(enrollment)
                .where(enrollment.id.eq(enrollId)
                        .and(enrollment.status.eq(EnrollmentStatus.WAITING)
                        .and(enrollment.member.id.eq(memberId))))
                .fetchFirst();

        return enroll;
    }

    @Override
    public boolean existJoinByMemberAndLecture(Long memberId, Long lectureId){
        LectureMember exist =  queryFactory.selectFrom(lectureMember)
                .where(lectureMember.member.id.eq(memberId)
                        .and(lectureMember.lecture.id.eq(lectureId)))
                .fetchFirst();

        return exist != null;
    }
}
