package com.didacto.repository.order;

import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import com.didacto.domain.Order;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.dto.order.OrderQueryFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderCustomRepository {
    List<Order> findOrderPage(Pageable pageable, OrderQueryFilter request);
    Long countOrders(OrderQueryFilter request);


    //포트원쪽에서 반환하는 값은 merchant_uid(String 값이다.)
    Optional<Order> findOrderAndPayment(String orderUid);

}
