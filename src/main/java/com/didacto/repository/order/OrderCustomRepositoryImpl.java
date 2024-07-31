package com.didacto.repository.order;

import com.didacto.domain.Lecture;
import com.didacto.domain.Order;
import com.didacto.domain.QOrder;
import com.didacto.domain.QPayment;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.dto.order.OrderQueryFilter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.didacto.domain.QLecture.lecture;
import static com.didacto.domain.QLectureMember.lectureMember;
import static com.didacto.domain.QOrder.order;
import static com.didacto.domain.QPayment.payment;

@Repository
@AllArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository{
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Order> findOrderPage(Pageable pageable, OrderQueryFilter request) {
        JPAQuery<Order> query = queryWithFilter(request);

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(QOrder.order.getType(), QOrder.order.getMetadata());
            query.orderBy(new OrderSpecifier<>(order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, pathBuilder.get(order.getProperty())));
        }



        int offset = Math.max(0, (pageable.getPageNumber() - 1) * pageable.getPageSize()); // Offset이 0 미만이 되지 않도록 보장

        return query
                .offset(offset)
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countOrders(OrderQueryFilter request) {
        JPAQuery<Order> query = queryWithFilter(request);
        return query.fetchCount();
    }

    private JPAQuery<Order> queryWithFilter(OrderQueryFilter filter) {
        JPAQuery<Order> query = queryFactory.select(order)
                .from(order)
                .join(order.payment, payment)
                .where(
                        payment.status.eq("paid"), // payment status가 "paid"인 경우 필터링
                        filter.getMember_id() != null ? order.member.id.eq(filter.getMember_id()) : null
                );
        return query;
    }

    @Override
    public Optional<Order> findOrderAndPayment(String orderUid) {
        QOrder order = QOrder.order;
        QPayment payment = QPayment.payment;
        JPAQuery<Order> query = queryFactory.select(order);

        Order result = query.select(order)
                .from(order)
                .leftJoin(order.payment, payment).fetchJoin()
                .where(order.orderUid.eq(orderUid))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
