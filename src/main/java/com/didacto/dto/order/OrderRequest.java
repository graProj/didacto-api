package com.didacto.dto.order;

import com.didacto.domain.Grade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotBlank(message = "원하는 등급을 입력해주세요.")
    @Schema(description = "등급", example = "Premium")
    @Pattern(regexp = "^(Freeteer|Silver|Gold|Premium)$", message = "등급은 Freeteer, Silver, Gold,Premium 중 하나여야 합니다.")
    private String itemName;
}
