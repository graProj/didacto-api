package com.didacto.repository.order;

import com.didacto.domain.*;
import com.didacto.dto.order.OrderQueryFilter;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.payment.PaymentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @DisplayName("orderUid를 통해 order를 찾을 수 있다.")
    @Test
        public void findOrderAndPayment() throws Exception{
        //given
        Member member = CreateMember("gildong1@naver.com","gildong123!@","회원1",false);

        Payment payment = Payment.builder()
                .status(PaymentStatus.READY)
                .price(1000L)
                .build();


        Order order = Order.builder()
                .member(member)
                .payment(payment)
                .orderUid("ABC123")
                .itemName(Grade.Premium)
                .price(1000L)
                .build();

        memberRepository.save(member);
        paymentRepository.save(payment);
        orderRepository.save(order);


        //when
        Optional<Order> abc123 = orderRepository.findOrderAndPayment("ABC123");

        //then
        assertThat(abc123.get().getId()).isEqualTo(order.getId());
        assertThat(abc123.get().getPayment()).isEqualTo(order.getPayment());
        assertThat(abc123.get().getOrderUid()).isEqualTo(order.getOrderUid());
        assertThat(abc123.get().getMember()).isEqualTo(order.getMember());
        assertThat(abc123.get().getItemName()).isEqualTo(Grade.Premium);
        assertThat(abc123.get().getPrice()).isEqualTo(1000L);


        }

    @DisplayName("결제 완료된 주문의 갯수를 셀수있다.")
    @Test
        public void countOrders() throws Exception{
        //given

        Member member = CreateMember("gildong1@naver.com","gildong123!@","회원1",false);

        Payment payment1 = CreatePayment(PaymentStatus.PAID, 1000L);
        Payment payment2 = CreatePayment(PaymentStatus.PAID, 1000L);
        Payment payment3 = CreatePayment(PaymentStatus.PAID, 1000L);

        Order order1 = CreateOrder(member, payment1, "ABC123", Grade.Premium, 1000L);
        Order order2 = CreateOrder(member, payment2, "ABC123", Grade.Premium, 1000L);
        Order order3 = CreateOrder(member, payment3, "ABC123", Grade.Premium, 1000L);


        memberRepository.save(member);
        paymentRepository.saveAll(List.of(payment1,payment2,payment3));
        orderRepository.saveAll(List.of(order1,order2,order3));

        OrderQueryFilter orderQueryFilter = OrderQueryFilter.builder()
                .member_id(member.getId())
                .build();

        //when
        Long count = orderRepository.countOrders(orderQueryFilter);

        //then
        assertThat(count).isEqualTo(3L);
        }

    @DisplayName("결제가 완료되지 않은 주문의 갯수는 제외하고 셀수있다.")
    @Test
    public void NotPaid_countOrders() throws Exception{
        //given

        Member member = CreateMember("gildong1@naver.com","gildong123!@","회원1",false);

        Payment payment1 = CreatePayment(PaymentStatus.READY, 1000L);
        Payment payment2 = CreatePayment(PaymentStatus.PAID, 1000L);
        Payment payment3 = CreatePayment(PaymentStatus.PAID, 1000L);

        Order order1 = CreateOrder(member, payment1, "ABC123", Grade.Premium, 1000L);
        Order order2 = CreateOrder(member, payment2, "ABC123", Grade.Premium, 1000L);
        Order order3 = CreateOrder(member, payment3, "ABC123", Grade.Premium, 1000L);


        memberRepository.save(member);
        paymentRepository.saveAll(List.of(payment1,payment2,payment3));
        orderRepository.saveAll(List.of(order1,order2,order3));

        OrderQueryFilter orderQueryFilter = OrderQueryFilter.builder()
                .member_id(member.getId())
                .build();

        //when
        Long count = orderRepository.countOrders(orderQueryFilter);

        //then
        assertThat(count).isEqualTo(2L);
    }



    private Member CreateMember(String email, String password, String name, Boolean deleted){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .deleted(deleted)
                .build();
    }

    private Order CreateOrder(Member member, Payment payment, String OrderUid, Grade itemName, Long price){
        return Order.builder()
                .member(member)
                .payment(payment)
                .orderUid(OrderUid)
                .itemName(itemName)
                .price(price)
                .build();
    }

    private Payment CreatePayment(PaymentStatus paymentStatus, Long price){
        return Payment.builder()
                .status(paymentStatus)
                .price(price)
                .build();
    }

}