package com.didacto.dto.order;

import com.didacto.domain.Lecture;
import com.didacto.domain.Order;
import com.didacto.dto.PageInfoResponse;
import com.didacto.dto.PageResponse;
import com.didacto.dto.lecture.LectureResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageResponse extends PageResponse {
    private List<OrderResponse> orders;

    public OrderPageResponse(
            PageInfoResponse pageInfo,
            List<Order> orders
    ){
        super(pageInfo);
        this.orders = orders.stream()
                .map(OrderResponse::new)
                .toList();
    }
}
