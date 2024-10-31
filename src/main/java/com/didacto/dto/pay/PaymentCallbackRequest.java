package com.didacto.dto.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCallbackRequest {

    @NotBlank(message = "결제 고유 번호.")
    @Schema(description = "결제 고유 번호", example = "imp_895643808915")
    private String payment_uid; // 결제 고유 번호


    @NotBlank(message = "주문 고유 번호.")
    @Schema(description = "주문 고유 번호", example = "1ca63a53-fd66-496b-a2fc-c6a4ca7dd75f")
    private String order_uid; // 주문 고유 번호


    //private String itemName; // 주문아이템 이름
}