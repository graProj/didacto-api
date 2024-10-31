package com.didacto.repository.lecturemember;

import com.didacto.domain.LectureMember;
import com.didacto.dto.lecturemember.LectureMemberQueryFilter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.didacto.domain.QLectureMember.lectureMember;

@Repository
@AllArgsConstructor
public class LectureMemberCustomRepositoryImpl implements LectureMemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<LectureMember> findLectureMember(LectureMemberQueryFilter filter) {
        JPAQuery<LectureMember> query = queryWithFilter(filter);
        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public List<LectureMember> findLectureMembers(LectureMemberQueryFilter filter) {
        return queryFactory.selectDistinct(lectureMember)
                .from(lectureMember)
                .where(
                        filter.getLectureId() != null ? lectureMember.lecture.id.eq(filter.getLectureId()) : null,
                        filter.getMemberId() != null ? lectureMember.member.id.eq(filter.getMemberId()) : null,
                        filter.getMemberIds() != null ? lectureMember.member.id.in(filter.getMemberIds()) : null,
                        filter.getDeleted() != null ? lectureMember.deleted.eq(filter.getDeleted()) : null
                )
                .fetch();
    }

    @Override
    public List<LectureMember> findLectureMemberPage(Pageable pageable, LectureMemberQueryFilter filter) {
        JPAQuery<LectureMember> query = queryWithFilter(filter);

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(lectureMember.getType(), lectureMember.getMetadata());
            query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty())));
        }

        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize(); // 페이지네이션 Offset 계산

        return query
                .offset(offset)
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countLectureMembers(LectureMemberQueryFilter filter) {
        JPAQuery<LectureMember> query = queryWithFilter(filter);
        return query.fetchCount();
    }

    private JPAQuery<LectureMember> queryWithFilter(LectureMemberQueryFilter filter) {
        JPAQuery<LectureMember> query = queryFactory.selectDistinct(lectureMember)
                .from(lectureMember)
                .where(
                        filter.getLectureId() != null ? lectureMember.lecture.id.eq(filter.getLectureId()) : null,
                        filter.getMemberId() != null ? lectureMember.member.id.eq(filter.getMemberId()) : null,
                        filter.getDeleted() != null ? lectureMember.deleted.eq(filter.getDeleted()) : null
                );
        return query;
    }
}
