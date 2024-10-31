package com.didacto.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "Pagenation : 페이징 정보")
public class PageInfoResponse {

    @Schema(description = "현재 페이지", example = "1")
    private long pageNo;

    @Schema(description = "한 페이지의 데이터 수", example = "10")
    private long pageSize;

    @Schema(description = "총 데이터 수", example = "55")
    private long totalElements;

    @Schema(description = "총 페이지 수", example = "6")
    private long totalPages;

    @Schema(description = "다음 페이지 존재여부", example = "true")
    private Boolean haveNext;
}
