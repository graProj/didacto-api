package com.didacto.dto.pay;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PaymentResponse {

    @NotBlank(message = "결제 고유번호")
    private Long id;
    private String user_name;
    private String item_name;
    private Long price;
    private String status;

}
