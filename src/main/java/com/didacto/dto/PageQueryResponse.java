package com.didacto.dto;

import com.didacto.dto.enrollment.PageInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageQueryResponse {
    PageInfoResponse pageInfo;
}
