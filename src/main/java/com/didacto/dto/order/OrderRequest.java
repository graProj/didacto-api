package com.didacto.dto.order;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @Pattern(regexp = "^(Freeteer|Silver|Gold)$", message = "계정 타입은 USER, ADMIN 중 하나여야 합니다.")
    private String itemName;
}
