package com.didacto.controller.v1.pay;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonResponse;

import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Order;
import com.didacto.dto.pay.PayResponse;
import com.didacto.dto.pay.PaymentCallbackRequest;
import com.didacto.repository.order.OrderRepository;
import com.didacto.service.order.OrderService;
import com.didacto.service.payment.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    @GetMapping("/payment/{orderId}")
    public CommonResponse<PayResponse> paymentPage(@PathVariable("orderId") Long order_id) {
        String orderUid = queryOne(order_id).getOrderUid();
        PayResponse payResponse = paymentService.findRequestDto(orderUid);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "결재내역을 조회하였습니다.",
                payResponse
        );
    }

    @ResponseBody
    @PostMapping("/payment")
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
        log.info("결제 응답={}", iamportResponse.getResponse().toString());
        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }

    @GetMapping("/success-payment")
    public String successPaymentPage() {
        return "success-payment";
    }

    @GetMapping("/fail-payment")
    public String failPaymentPage() {
        return "fail-payment";
    }


    public Order queryOne(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.ORDER_NOT_FOUND));


    }
}
