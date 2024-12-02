//package com.didacto.service.payment;
//
//import com.didacto.domain.*;
//import com.didacto.dto.pay.PaymentCallbackRequest;
//import com.didacto.repository.member.MemberRepository;
//import com.didacto.repository.order.OrderRepository;
//import com.didacto.repository.payment.PaymentRepository;
//import com.siot.IamportRestClient.IamportClient;
//import com.siot.IamportRestClient.response.IamportResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.BDDMockito;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.*;
//
//
////TODO paymentService 의 iamportResponse값이 moking처리가 잘 안됨 해결해야함
//@Transactional
//@SpringBootTest
//@ActiveProfiles("test")
//class PaymentServiceTest {
//
//    @Mock
//    IamportClient iamportClient;
//
//    @Mock
//    OrderRepository orderRepository;
//
//    @Mock
//    PaymentRepository paymentRepository;
//
//    @InjectMocks
//    PaymentService paymentService;
//
//    @Mock
//    MemberRepository memberRepository;
//
//    @BeforeEach
//    void setUp() {
//        paymentService = new PaymentService(orderRepository, paymentRepository, memberRepository);
//        // Mock 객체를 PaymentService의 iamportClient 필드에 수동으로 주입
//        paymentService.iamportClient = iamportClient;
//    }
//
//
//    @Test
//    void paymentByCallbackTest() throws Exception {
//        // Given
//        PaymentCallbackRequest request = new PaymentCallbackRequest();
//        request.setImp_uid("imp_123456789");
//        request.setMerchant_uid("merchant_123456");
//
//        // IamportResponse와 Payment Mock 객체 생성
//        Payment payment = Payment.builder()
//                .status(PaymentStatus.READY)
//                .price(1000L)
//                .paymentUid("imp_123456789")
//                .build();
//
//        IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = mock(IamportResponse.class);
//        com.siot.IamportRestClient.response.Payment iamportPayment = mock(com.siot.IamportRestClient.response.Payment.class);
//
//        Member member = CreateMember("gildong1@naver.com", "gildong123!@", "회원1", false);
//
//        Order order = Order.builder()
//                .member(member)
//                .payment(payment)
//                .orderUid("ABC123")
//                .itemName(Grade.Premium)
//                .price(1000L)
//                .build();
//
//        // Mocking 설정
//        given(iamportClient.paymentByImpUid(request.getImp_uid())).willReturn(iamportResponse);
//        given(orderRepository.findOrderAndPayment(request.getMerchant_uid())).willReturn(Optional.of(order));
//
//        // iamportResponse가 iamportPayment를 반환하도록 설정
//        given(iamportResponse.getResponse()).willReturn(iamportPayment);
//
//        // iamportPayment의 상태와 금액을 설정
//        given(iamportPayment.getStatus()).willReturn("paid");
//        given(iamportPayment.getAmount()).willReturn(BigDecimal.valueOf(1000)); // double 타입 반환
//
//        // When
//        IamportResponse<com.siot.IamportRestClient.response.Payment> response = paymentService.paymentByCallback(request);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("paid", response.getResponse().getStatus());
//        assertEquals(1000, response.getResponse().getAmount().intValue());
//    }
//
//    private Member CreateMember(String email, String password, String name, Boolean deleted){
//        return Member.builder()
//                .email(email)
//                .password(password)
//                .name(name)
//                .deleted(deleted)
//                .build();
//    }
//}