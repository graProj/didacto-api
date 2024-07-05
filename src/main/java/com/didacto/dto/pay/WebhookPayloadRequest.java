package com.didacto.dto.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WebhookPayloadRequest {

    @NotBlank(message = "고유 결제 ID")
    @Schema(description = "고유 결제 ID", example = "imp_123456789")
    private String impUid;

    @NotBlank(message = "주문 고유 번호")
    @Schema(description = "주문 고유 번호", example = "643c7b2d-748a-4e0b-9b47-7de6f8fd2da7")
    private String merchantUid;

    @NotBlank(message = "결제 상태")
    @Schema(description = "결제 상태", example = "paid")
    private String status;

    @Schema(description = "결제 금액", example = "1000")
    private int amount;

    @Builder
    public WebhookPayloadRequest(String impUid, String merchantUid, String status, int amount) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.status = status;
        this.amount = amount;
    }
}