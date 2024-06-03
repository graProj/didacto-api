package com.didacto.repository.order;

import com.didacto.domain.Member;
import com.didacto.domain.Order;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderCustomRepository {
    Optional<Order> findOrderAndPaymentAndMember(String orderUid);
    Optional<Order> findOrderAndPayment(String orderUid);

}
