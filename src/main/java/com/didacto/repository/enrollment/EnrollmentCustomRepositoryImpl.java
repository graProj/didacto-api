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
    public boolean isHaveWaitingEnrollmentByMemberId(Long memberId){
        Enrollment enroll =  queryFactory.selectFrom(enrollment)
                .where(enrollment.member.id.eq(memberId)
                        .and(enrollment.status.eq(EnrollmentStatus.WAITING)))
                .fetchFirst();

        return enroll != null;
    }

    @Override
    public boolean alreadyJoinedCheck(Long memberId, Long lectureId){
        LectureMember exist =  queryFactory.selectFrom(lectureMember)
                .where(lectureMember.member.id.eq(memberId)
                        .and(lectureMember.lecture.id.eq(lectureId)))
                .fetchFirst();

        return exist != null;
    }
}
