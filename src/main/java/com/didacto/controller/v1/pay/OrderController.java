package com.didacto.controller.v1.pay;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.dto.order.OrderRequest;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.order.OrderRepository;
import com.didacto.service.member.MemberQueryService;
import com.didacto.service.member.MemberService;
import com.didacto.service.order.OrderCommanService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class OrderController {
    private final MemberService memberService;
    private final MemberQueryService memberQueryService;
    private final OrderCommanService orderService;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;


    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "ORDER_01 : 주문 생성 API", description = "주문을 생성한다.")
    @PostMapping("/order")
    public CommonResponse<Long> create(
            @RequestBody OrderRequest request
    ) {
        Long orderId = orderService.create(request);
        return new CommonResponse(
                true, HttpStatus.CREATED, null, orderId
        );
    }


}
