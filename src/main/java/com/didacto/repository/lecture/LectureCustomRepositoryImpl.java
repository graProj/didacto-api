package com.didacto.repository.lecture;


import com.didacto.domain.Lecture;
import com.didacto.dto.lecture.LectureQueryFilter;
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

import static com.didacto.domain.QLecture.lecture;


@Repository
@AllArgsConstructor
public class LectureCustomRepositoryImpl implements LectureCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Lecture> findLecturePage(Pageable pageable, LectureQueryFilter request) {
        JPAQuery<Lecture> query = queryWithFilter(request);

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(lecture.getType(), lecture.getMetadata());
            query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty())));
        }

        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize(); // 페이지네이션 Offset 계산

        return query
                .offset(offset)
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countLectures(LectureQueryFilter request) {
        JPAQuery<Lecture> query = queryWithFilter(request);
        return query.fetchCount();
    }

    private JPAQuery<Lecture> queryWithFilter(LectureQueryFilter filter) {
        JPAQuery<Lecture> query = queryFactory.select(lecture)
                .from(lecture)
                .where(
                        filter.getTitleKeyword() != null ? lecture.title.like("%" + filter.getTitleKeyword() +"%") : null,
                        filter.getDeleted() != null ? lecture.deleted.eq(filter.getDeleted()) : null
                );
        return query;
    }
}
