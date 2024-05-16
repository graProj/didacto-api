package com.didacto.repository.enrollment;


import com.didacto.domain.Enrollment;
import com.didacto.dto.enrollment.EnrollmentQueryFilter;
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

import static com.didacto.domain.QEnrollment.enrollment;


@Repository
@AllArgsConstructor
public class EnrollmentCustomRepositoryImpl implements EnrollmentCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Enrollment> findEnrollment(EnrollmentQueryFilter request) {
        JPAQuery<Enrollment> query = queryWithFilter(request);
        return Optional.ofNullable(query.fetchFirst());
    }

    @Override
    public List<Enrollment> findEnrollmentPage(Pageable pageable, EnrollmentQueryFilter request) {
        JPAQuery<Enrollment> query = queryWithFilter(request);

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(enrollment.getType(), enrollment.getMetadata());
            query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty())));
        }

        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize(); // 페이지네이션 Offset 계산

        return query
                .offset(offset)
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countEnrollments(EnrollmentQueryFilter request) {
        JPAQuery<Enrollment> query = queryWithFilter(request);
        return query.fetchCount();
    }

    private JPAQuery<Enrollment> queryWithFilter(EnrollmentQueryFilter filter) {
        JPAQuery<Enrollment> query = queryFactory.select(enrollment)
                .from(enrollment)
                .where(
                        filter.getIds() != null ? enrollment.id.in(filter.getIds()) : null,
                        filter.getStatuses() != null ? enrollment.status.in(filter.getStatuses()) : null,
                        filter.getMemberId() != null ? enrollment.member.id.eq(filter.getMemberId()) : null,
                        filter.getLectureId() != null ? enrollment.lecture.id.eq(filter.getLectureId()) : null,
                        filter.getTutorId() != null ? enrollment.lecture.owner.id.eq(filter.getTutorId()) : null
                );
        return query;
    }
}
