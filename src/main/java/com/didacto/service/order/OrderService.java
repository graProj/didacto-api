package com.didacto.service.order;

import com.didacto.domain.Member;
import com.didacto.domain.Order;
import com.didacto.domain.Payment;
import com.didacto.domain.PaymentStatus;
import com.didacto.repository.order.OrderCustomRepository;
import com.didacto.repository.order.OrderRepository;
import com.didacto.repository.pament.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService implements OrderCustomRepository {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Order autoOrder(Member member) {
         Payment payment = Payment.builder()
                .price(1000L)
                .status(PaymentStatus.READY)
                .build();
        paymentRepository.save(payment);


        Order order = Order.builder()
                .member(member)
                .price(1000L)
                .itemName("실버")
                .orderUid(UUID.randomUUID().toString())
                .payment(payment)
                .build();

        return orderRepository.save(order);

    }
}
