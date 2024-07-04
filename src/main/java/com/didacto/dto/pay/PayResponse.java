package com.didacto.dto.pay;

import com.didacto.domain.Grade;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayResponse {

    @NotBlank(message = "주문 고유 번호.")
    @Schema(description = "주문 고유 번호", example = "1ca63a53-fd66-496b-a2fc-c6a4ca7dd75f")
    private String orderUid;
    private Grade itemName;
    private String buyerName;
    private Long paymentPrice;
    private String buyerEmail;

    @Builder
    public PayResponse(String orderUid, Grade itemName, String buyerName, Long paymentPrice, String buyerEmail) {
        this.orderUid = orderUid;
        this.itemName = itemName;
        this.buyerName = buyerName;
        this.paymentPrice = paymentPrice;
        this.buyerEmail = buyerEmail;
    }
   }
