package com.didacto.repository.order;

import com.didacto.domain.Order;
import com.didacto.domain.QMember;
import com.didacto.domain.QOrder;
import com.didacto.domain.QPayment;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

public class OrderRepositoryImpl implements OrderCustomRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Order> findOrderAndPaymentAndMember(String orderUid) {
        JPAQuery<Order> query = new JPAQuery<>(em);
        QOrder order = QOrder.order;
        QPayment payment = QPayment.payment;
        QMember member = QMember.member;

        Order result = query.select(order)
                .from(order)
                .leftJoin(order.payment, payment).fetchJoin()
                .leftJoin(order.member, member).fetchJoin()
                .where(order.orderUid.eq(orderUid))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Order> findOrderAndPayment(String orderUid) {
        JPAQuery<Order> query = new JPAQuery<>(em);
        QOrder order = QOrder.order;
        QPayment payment = QPayment.payment;

        Order result = query.select(order)
                .from(order)
                .leftJoin(order.payment, payment).fetchJoin()
                .where(order.orderUid.eq(orderUid))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
