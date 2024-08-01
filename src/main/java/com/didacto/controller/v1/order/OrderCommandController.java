package com.didacto.controller.v1.order;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.AuthConstant;
import com.didacto.dto.order.OrderRequest;
import com.didacto.service.order.OrderCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
@Tag(name = "ORDER API", description = "주문 API")
public class OrderCommandController {
    private final OrderCommandService orderService;


    @PreAuthorize(AuthConstant.AUTH_ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "ORDER_01 : 주문 생성 API", description = "주문을 생성한다.")
    @PostMapping("")

    public CommonResponse<Long> create(
            @RequestBody OrderRequest request
    ) {
        Long orderId = orderService.create(request);
        return new CommonResponse(
                true, HttpStatus.CREATED, null, orderId
        );
    }


}
