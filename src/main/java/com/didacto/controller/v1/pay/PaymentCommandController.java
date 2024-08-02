package com.didacto.controller.v1.pay;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.dto.pay.PaymentCallbackRequest;
import com.didacto.service.order.OrderQueryService;
import com.didacto.service.payment.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "PAYMENT API", description = "결제, 웹훅 API")
public class PaymentCommandController {
    private final PaymentService paymentService;
    private final OrderQueryService orderQueryService;


    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "PAYMENT_02 : 결제 API", description = "결제를 진행한다.")
    @PostMapping("/payment")
    public CommonResponse<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
        log.info("결제 응답={}", iamportResponse.getResponse().toString());
        return new CommonResponse<>(
                true, HttpStatus.OK, null, iamportResponse
        );
    }


    @Operation(summary = "PAYMENT_03 : 웹훅 API", description = "웹훅을 통해 결제를 진행한다.")
    @PostMapping("/webhook")
    public CommonResponse<IamportResponse<Payment>> handleWebhook(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
        log.info("결제 응답={}", iamportResponse.getResponse().toString());
        return new CommonResponse<>(
                true, HttpStatus.OK, null, iamportResponse
        );
    }


}
