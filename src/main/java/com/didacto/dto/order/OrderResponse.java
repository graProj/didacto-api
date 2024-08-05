package com.didacto.dto.order;

import com.didacto.domain.Grade;
import com.didacto.domain.Order;
import com.didacto.dto.member.MemberResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {

    @NotBlank(message = "주문 고유 번호.")
    @Schema(description = "주문 고유 번호", example = "1ca63a53-fd66-496b-a2fc-c6a4ca7dd75f")
    private String orderUid;
    private Grade itemName;
    private String buyerName;
    private Long paymentPrice;
    private String buyerEmail;
    private OffsetDateTime paymentTime;

    public OrderResponse(Order order) {
        this.orderUid = order.getOrderUid();
        this.itemName = order.getItemName();
        this.buyerName = order.getMember().getName();
        this.paymentPrice = order.getPrice();
        this.buyerEmail = order.getMember().getEmail();
        this.paymentTime = order.getPayment().getModifiedTime();
    }
   }
