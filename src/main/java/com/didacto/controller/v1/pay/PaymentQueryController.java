package com.didacto.controller.v1.pay;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Order;
import com.didacto.dto.order.OrderPageResponse;
import com.didacto.dto.order.OrderQueryFilter;
import com.didacto.dto.order.OrderQueryRequest;
import com.didacto.dto.order.OrderResponse;
import com.didacto.service.order.OrderQueryService;
import com.didacto.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "PAYMENT API", description = "결제 관련 조회 API")
public class PaymentQueryController {
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
}
