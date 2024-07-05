package com.didacto.service.payment;

import com.didacto.domain.Order;
import com.didacto.dto.pay.PayResponse;
import com.didacto.dto.pay.PaymentCallbackRequest;
import com.didacto.dto.pay.WebhookPayloadRequest;
import com.didacto.repository.order.OrderRepository;
import com.didacto.repository.pament.PaymentRepository;
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

    public PayResponse findRequestDto(String orderUid) {
        Order order = orderRepository.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        return PayResponse.builder()
                .buyerName(order.getMember().getName())
                .buyerEmail(order.getMember().getEmail())
                .paymentPrice(order.getPayment().getPrice())
                .itemName(order.getItemName())
                .orderUid(order.getOrderUid())
                .build();
    }

    @Transactional
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPayment_uid());
            // 주문내역 조회
            Order order = orderRepository.findOrderAndPayment(request.getOrder_uid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 상태 검증 및 처리
            validateAndProcessPayment(iamportResponse, order);

            return iamportResponse;

        } catch (IamportResponseException e) {
            log.error("IamportResponseException 발생", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("IOException 발생", e);
            throw new RuntimeException(e);
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
                        .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));
            }

            // 결제 상태 검증 및 처리
            validateAndProcessPayment(iamportResponse, order);

        } catch (IamportResponseException | IOException e) {
            log.error("IamportResponseException 또는 IOException 발생", e);
            throw new RuntimeException(e);
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
            throw new RuntimeException("결제 미완료");
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

            throw new RuntimeException("결제금액 위변조 의심");
        }

        // 결제 상태 변경
        order.getPayment().changePaymentBySuccess(iamportResponse.getResponse().getStatus(), iamportResponse.getResponse().getImpUid());
        order.getMember().premium();
    }
}
