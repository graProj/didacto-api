package com.didacto.service.order;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.PreconditionFailException412;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.*;
import com.didacto.dto.auth.SignUpRequest;
import com.didacto.dto.order.OrderRequest;
import com.didacto.repository.member.MemberRepository;
import com.didacto.repository.order.OrderCustomRepository;
import com.didacto.repository.order.OrderRepository;
import com.didacto.repository.pament.PaymentRepository;
import com.didacto.service.member.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberQueryService memberQueryService;
    private final PaymentRepository paymentRepository;

    public Long create(OrderRequest req) {
        Order order = createFormOfOrder(req);
        Order result = orderRepository.save(order);
        return result.getId();

    }

    private Order createFormOfOrder(OrderRequest req) {


        Grade grade = null;
        Long price = 0L;

        //일단 상품을 입력받아서 저장하는 식으로 작성 나중에 정적테이블을 만들어서 연관시키는 방법도 고려중
        if (req.getItemName().equals("Premium")) {
            grade = Grade.Premium;
            price = 1000L;
        }else {
            throw new PreconditionFailException412(ErrorDefineCode.ORDER_GRADE_FAIL);
        }

        Long member_id = SecurityUtil.getCurrentMemberId();
        Member member = memberQueryService.query(member_id);

        //결제내역 생성
        Payment payment = Payment.builder()
                .price(price)
                .status("Ready")
                .build();

        paymentRepository.save(payment);

        Order order = Order.builder()
                .member(member)
                .price(price)
                .itemName(grade)
                .orderUid(UUID.randomUUID().toString())
                .payment(payment)
                .build();

        return order;
    }
}
