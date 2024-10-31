package com.didacto.service.order;

import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.exception.custom.exception.PreconditionFailException412;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Grade;
import com.didacto.domain.Member;
import com.didacto.domain.Order;
import com.didacto.dto.order.OrderRequest;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.order.OrderRepository;
import com.didacto.repository.payment.PaymentRepository;
import com.didacto.service.member.MemberQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class OrderCommandServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;


    @Autowired
    OrderCommandService orderCommandService;

    @DisplayName("주문을 생성한다.")
    @Test
        public void createOrder() throws Exception {
        //given
        Member member1 = Member.builder()
                .email("gildong@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .deleted(false)
                .grade(Grade.Freeteer)
                .build();
        memberRepository.save(member1);

        OrderRequest orderRequest = new OrderRequest("Premium");


        //when
        try (MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(member1.getId());
            // when
            Long orderId = orderCommandService.create(orderRequest);

            Optional<Order> saveOrderId = orderRepository.findById(orderId);

            //then

            assertThat(orderId).isEqualTo(saveOrderId.get().getId());

        }

    }

    @DisplayName("회원이 이미 Premium인 상태면 예외를 터뜨린다.")
    @Test
    public void AlreadyPremiumcreateOrder() throws Exception {
        //given
        Member member1 = Member.builder()
                .email("gildong@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .deleted(false)
                .grade(Grade.Premium)
                .build();
        memberRepository.save(member1);

        OrderRequest orderRequest = new OrderRequest("Premium");


        //when
        try (MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            // SecurityUtil.getCurrentMemberId() 모킹
            Mockito.when(SecurityUtil.getCurrentMemberId()).thenReturn(member1.getId());

            // memberQueryService는 정적 클래스가 아니므로 mock 생성
            MemberQueryService memberQueryService = Mockito.mock(MemberQueryService.class);
            Mockito.when(memberQueryService.query(member1.getId())).thenReturn(member1);

            //then
            assertThatThrownBy(() -> orderCommandService.create(orderRequest))
                    .isInstanceOf(PreconditionFailException412.class)
                    .hasMessage("이미 PREMIUM 등급입니다.");

        }

    }


    @DisplayName("Premium을 주문하지 않고 다른 것을 주문하면 예외를 터뜨린다.")
    @Test
    public void AnotherPremiumcreateOrder() throws Exception {
        //given
        Member member1 = Member.builder()
                .email("gildong@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .deleted(false)
                .grade(Grade.Freeteer)
                .build();
        memberRepository.save(member1);

        OrderRequest orderRequest = new OrderRequest("Gold");


        //when
        try (MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            // SecurityUtil.getCurrentMemberId() 모킹
            Mockito.when(SecurityUtil.getCurrentMemberId()).thenReturn(member1.getId());

            // memberQueryService는 정적 클래스가 아니므로 mock 생성
            MemberQueryService memberQueryService = Mockito.mock(MemberQueryService.class);
            Mockito.when(memberQueryService.query(member1.getId())).thenReturn(member1);

            //then
            assertThatThrownBy(() -> orderCommandService.create(orderRequest))
                    .isInstanceOf(PreconditionFailException412.class)
                    .hasMessage("주문된 상품이 프리미엄이 아닙니다.");

        }

    }


}