package com.didacto.dto.order;

import com.didacto.dto.PageQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderQueryRequest extends PageQueryRequest {
    private Long member_id;
    private String status;
}
