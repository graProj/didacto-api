package com.didacto.controller.v1.pay;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonResponse;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Order;
import com.didacto.dto.lecture.LecturePageResponse;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.dto.lecture.LectureQueryRequest;
import com.didacto.dto.order.OrderPageResponse;
import com.didacto.dto.order.OrderQueryFilter;
import com.didacto.dto.order.OrderQueryRequest;
import com.didacto.dto.order.OrderResponse;
import com.didacto.dto.pay.PaymentCallbackRequest;
import com.didacto.repository.order.OrderRepository;
import com.didacto.service.order.OrderQueryService;
import com.didacto.service.payment.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderQueryService orderQueryService;

    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "PAYMENT_01 : 결제 데이터 조회", description = "결제에 필요한 데이터를 조회한다.")
    @GetMapping("/payment/{orderId}")
    public CommonResponse<OrderResponse> paymentPage(@PathVariable("orderId") Long order_id) {
        Order order = orderQueryService.query(order_id);

        return new CommonResponse(
                true, HttpStatus.OK, "결재내역을 조회하였습니다.", new OrderResponse(order)
        );
    }

    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @GetMapping("/payment/list")
    @Operation(summary = "PAYMENT_02 : 결제 목록 조회", description = "결제가 완료된 항목을 조회한다.")
    public CommonResponse<OrderPageResponse> queryPage(
            @ParameterObject OrderQueryRequest request
    ){
        Long memberId = SecurityUtil.getCurrentMemberId();
        OrderQueryFilter filter = OrderQueryFilter.builder()
                .member_id(memberId)
                .build();

        OrderPageResponse orderPageResponse = orderQueryService.queryPage(request.getPageable(), filter);

        return new CommonResponse(
                true,
                HttpStatus.OK,
                "결제 목록을 조회하였습니다.",
                orderPageResponse
        );
    }





//    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @Operation(summary = "PAYMENT_02 : 결제 API", description = "결제를 진행한다.")
    @PostMapping("/payment")
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
        log.info("결제 응답={}", iamportResponse.getResponse().toString());
        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }


//    @PostMapping("/webhook")
//    public ResponseEntity<String> handleWebhook(@RequestBody WebhookPayloadRequest payload) {
//            paymentService.processWebhookPayment(payload);
//            return ResponseEntity.ok("Webhook received");
//    }


}
