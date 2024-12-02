package com.didacto.service.order;

import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.*;
import com.didacto.dto.order.OrderPageResponse;
import com.didacto.dto.order.OrderQueryFilter;
import com.didacto.dto.order.OrderQueryRequest;
import com.didacto.dto.order.OrderResponse;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.order.OrderRepository;
import com.didacto.repository.payment.PaymentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@ActiveProfiles("test")
class OrderQueryServiceTest {

    @Autowired
    OrderQueryService orderQueryService;

    @Autowired
    OrderRepository orderRepository;


    @DisplayName("특정 id에 해당하는 주문을 찾는다.")
    @Test
    public void query() throws Exception{
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

        orderRepository.save(order);


        //when
        Order query = orderQueryService.query(order.getId());

        //then
        assertThat(query)
                .extracting("orderUid", "itemName", "price")
                .contains("ABC123", Grade.Premium, 1000L);

    }

    @DisplayName("특정 id에 해당하는 주문이 없다면 예외를 발생시킨다.")
    @Test
        public void queryNotFound() throws Exception{
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

        orderRepository.save(order);

        Long nonExistentId = -1L;

        //when, then
        assertThatThrownBy(() -> orderQueryService.query(nonExistentId))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("주문을 찾을 수 없습니다.");

        }



    private Member CreateMember(String email, String password, String name, Boolean deleted){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .deleted(deleted)
                .build();
    }
}