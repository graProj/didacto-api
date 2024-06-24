package com.didacto.dto.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WebhookRequest {
    private String imp_Uid; // 결제 고유 번호
    private String merchant_Uid; // 주문 고유 번호
}
