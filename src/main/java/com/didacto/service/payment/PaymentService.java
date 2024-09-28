package com.didacto.service.payment;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NetworkException503;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.exception.custom.exception.PreconditionFailException412;
import com.didacto.config.exception.custom.exception.TimeOutException408;
import com.didacto.domain.Member;
import com.didacto.domain.Order;
import com.didacto.domain.PaymentStatus;
import com.didacto.dto.pay.PaymentCallbackRequest;
import com.didacto.dto.pay.WebhookPayloadRequest;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.order.OrderRepository;
import com.didacto.repository.payment.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private IamportClient iamportClient;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @Value("${payment.test-mode}")
    private boolean testMode;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }


    @Transactional
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getImp_uid());
            if (iamportResponse == null) {
                throw new NoSuchElementFoundException404(ErrorDefineCode.PAYMENT_NOT_FOUND_HISTORY);
            }
            // 주문내역 조회
            Order order = orderRepository.findOrderAndPayment(request.getMerchant_uid())
                    .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.ORDER_NOT_FOUND));

            // 결제 상태 검증 및 처리
            validateAndProcessPayment(iamportResponse, order);

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new NetworkException503(ErrorDefineCode.IAMPORT_NOT_FOUND_RESPONSE);
        } catch (IOException e) {
            throw new NetworkException503(ErrorDefineCode.IAMPORT_NETWORT_ERROR);
        }
    }

    @Transactional
    public void processWebhookPayment(WebhookPayloadRequest payload) {
        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(payload.getImpUid());
            // 주문내역 조회
            Order order;
            if (testMode) {
                // 테스트 모드일 경우, 임시 주문 생성
                order = createTestOrder(payload);
            } else {
                order = orderRepository.findOrderAndPayment(payload.getMerchantUid())
                        .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.ORDER_NOT_FOUND));
            }

            // 결제 상태 검증 및 처리
            validateAndProcessPayment(iamportResponse, order);

        } catch (IamportResponseException e) {
            throw new NetworkException503(ErrorDefineCode.IAMPORT_NOT_FOUND_RESPONSE);
        } catch (IOException e) {
            throw new NetworkException503(ErrorDefineCode.IAMPORT_NETWORT_ERROR);
        }
    }

    private Order createTestOrder(WebhookPayloadRequest payload) {
        // 테스트용 임시 주문 객체 생성
        Order order = new Order();
        order.setOrder(payload);
        return order;
    }

    private void validateAndProcessPayment(IamportResponse<Payment> iamportResponse, Order order) throws IamportResponseException, IOException {
        // 결제 완료가 아니면
        if (!"paid".equals(iamportResponse.getResponse().getStatus())) {
            // 주문, 결제 삭제
            orderRepository.delete(order);
            paymentRepository.delete(order.getPayment());
            throw new PreconditionFailException412(ErrorDefineCode.PAYMENT_NOT_COMPLETE);
        }

        // DB에 저장된 결제 금액
        Long price = order.getPayment().getPrice();
        // 실 결제 금액
        int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

        // 결제 금액 검증
        if (iamportPrice != price) {
            // 주문, 결제 삭제
            orderRepository.delete(order);
            paymentRepository.delete(order.getPayment());

            // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
            iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

            throw new PreconditionFailException412(ErrorDefineCode.PAYMENT_DIFFERENT_AMOUNT);
        }

        // 결제 상태 변경
        order.getPayment().changePaymentBySuccess(PaymentStatus.PAID, iamportResponse.getResponse().getImpUid());

        Member member = order.getMember();
        member.premium();
        memberRepository.save(member);
    }
}
