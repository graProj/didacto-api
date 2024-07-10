package com.didacto.service.order;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Lecture;
import com.didacto.domain.Order;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.lecture.LecturePageResponse;
import com.didacto.dto.lecture.LectureQueryFilter;
import com.didacto.dto.order.OrderPageResponse;
import com.didacto.dto.order.OrderQueryFilter;
import com.didacto.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderQueryService {
    private final OrderRepository orderRepository;

    public Order query(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementFoundException404(ErrorDefineCode.ORDER_NOT_FOUND));
    }


    public OrderPageResponse queryPage(Pageable pageable, OrderQueryFilter request) {
        long page = pageable.getOffset();
        long size = pageable.getPageSize();

        // Query : 페이지네이션 및 조건 필터링
        List<Order> orders = orderRepository.findOrderPage(pageable, request);

        // Query : Pagenation을 위한 총 개수 집계
        long count = orderRepository.countOrders(request);

        // Calc : 총 페이지 수와 다음 페이지 존재 여부 계산
        long totalPage = (long) Math.ceil((double) count / size);
        boolean isHaveNext = page < totalPage;

        // Out
        PageInfoResponse pageInfo = PageInfoResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(totalPage)
                .totalElements(count)
                .haveNext(isHaveNext)
                .build();

        return new OrderPageResponse(pageInfo, orders);
    }

}
