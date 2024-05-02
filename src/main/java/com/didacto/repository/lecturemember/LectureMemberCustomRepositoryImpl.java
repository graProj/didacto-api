package com.didacto.repository.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.didacto.domain.QLectureMember.lectureMember;

@Repository
@AllArgsConstructor
public class LectureMemberCustomRepositoryImpl implements LectureMemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<LectureMember> findLectureMembersWithFilter(LectureMemberQueryFilter filter) {
        return queryFactory.selectDistinct(lectureMember)
                .from(lectureMember)
                .where(lectureMember.lecture.id.eq(filter.getLectureId())
                        .and(lectureMember.member.id.eq(filter.getMemberId()))
                        .and(lectureMember.deleted.eq(filter.getDeleted()))
                )
                .fetch();
    }
}
