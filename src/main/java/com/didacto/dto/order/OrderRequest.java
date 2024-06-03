package com.didacto.dto.order;

import com.didacto.domain.Grade;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @Pattern(regexp = "^(Freeteer|Silver|Gold|Premium)$", message = "등급은 Freeteer, Silver, Gold,Premium 중 하나여야 합니다.")
    private String itemName;
}
