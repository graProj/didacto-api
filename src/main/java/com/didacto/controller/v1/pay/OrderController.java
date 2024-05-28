package com.didacto.controller.v1.pay;

import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Member;
import com.didacto.domain.Order;
import com.didacto.repository.member.MemberRepository;
import com.didacto.service.member.MemberQueryService;
import com.didacto.service.member.MemberService;
import com.didacto.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Controller
@RequiredArgsConstructor
public class OrderController {
    private final MemberService memberService;
    private final MemberQueryService memberQueryService;
    private final OrderService orderService;
    private final MemberRepository memberRepository;

    @GetMapping("/order")
    public String order(@RequestParam(name = "message", required = false) String message,
                        @RequestParam(name = "orderUid", required = false) String id,
                        Model model) {

        model.addAttribute("message", message);
        model.addAttribute("orderUid", id);
        return "order";
    }

    @PostMapping("/order")
    public String autoOrder(){
        Long member_id = SecurityUtil.getCurrentMemberId();
        Member member = memberQueryService.query(member_id);
        Order order = orderService.autoOrder(member);


        String message = "주문 실패";
        if(order != null) {
            message = "주문 성공";
        }

        String encode = URLEncoder.encode(message, StandardCharsets.UTF_8);

        return "redirect:/order?message="+encode+"&orderUid="+order.getOrderUid();
    }



}
