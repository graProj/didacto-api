package com.didacto.controller.v1.pay;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonResponse;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Lecture;
import com.didacto.domain.Member;
import com.didacto.domain.Order;
import com.didacto.dto.order.OrderRequest;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.order.OrderRepository;
import com.didacto.repository.order.OrderRepositoryImpl;
import com.didacto.service.member.MemberQueryService;
import com.didacto.service.member.MemberService;
import com.didacto.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class OrderController {
    private final MemberService memberService;
    private final MemberQueryService memberQueryService;
    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/order")
    public Order queryOne(Long orderId) {
        return orderRepository.findById(orderId)
                        .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.ORDER_NOT_FOUND));

        
    }

    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
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
