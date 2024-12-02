package com.didacto.controller.v1.pay;

import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.controller.v1.enrollment.EnrollmentQueryController;
import com.didacto.domain.*;
import com.didacto.dto.order.OrderPageResponse;
import com.didacto.dto.order.OrderQueryFilter;
import com.didacto.repository.order.OrderRepository;
import com.didacto.service.enrollment.EnrollmentQueryService;
import com.didacto.service.order.OrderQueryService;
import com.didacto.service.payment.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentQueryController.class)
class PaymentQueryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderQueryService orderQueryService;

    @MockBean
    private PaymentService paymentService;



    @Test
    @WithMockUser(username = "user", roles = {AuthConstant.AUTH_ADMIN})
    void paymentPage() throws Exception {

        //given
        Member member = CreateMember("gildong1@naver.com", "gildong123!@", "회원1", false);

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


        given(orderQueryService.query(1L)).willReturn(order);


        try (MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(1L);
            //when, then
            mockMvc.perform(get("/api/v1/payment/{orderId}", 1L))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("결재내역을 조회하였습니다."));

        }
    }

    @Test
    @WithMockUser(username = "user", roles = {AuthConstant.AUTH_ADMIN})
    void queryPage() throws Exception {
        //given
        Member member = CreateMember("gildong1@naver.com", "gildong123!@", "회원1", false);

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

        OrderPageResponse orderPageResponse = new OrderPageResponse();

        given(orderQueryService.queryPage(any(), any())).willReturn(orderPageResponse);


        try (MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(1L);
            //when, then
            mockMvc.perform(get("/api/v1/payment/list", 1L))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("결제 목록을 조회하였습니다."))
                    .andExpect(jsonPath("$.response").isNotEmpty());


        }

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