package com.didacto.dto.lecture;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturePagingRequest {
    @Parameter(description = "조회할 페이지 (1부터 시작)", example = "1", required = true)
    private Long page;

    @Parameter(description = "한 페이지에 조회할 데이터 수", example = "10", required = true)
    private Long size;

}
