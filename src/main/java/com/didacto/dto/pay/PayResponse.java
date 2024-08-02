package com.didacto.dto.pay;

import com.didacto.domain.Grade;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayResponse {
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
