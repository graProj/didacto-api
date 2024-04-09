package com.didacto.dto.enrollment;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQueryConditionRequest {
    @Parameter(description = "조회할 페이지 (1부터 시작)", example = "1", required = true)
    private long page;

    @Parameter(description = "한 페이지에 조회할 데이터 수", example = "10", required = true)
    private long size;

    @Parameter(description = "정렬 기준(date, name)", example = "date", required = true)
    private String order;

    @Parameter(description = "수락 대기 상태 포함", example = "true", required = false)
    boolean waiting;

    @Parameter(description = "취소 상태 포함", example = "true", required = false)
    boolean canceled;

    @Parameter(description = "승인 상태 포함", example = "true", required = false)
    boolean accepted;

    @Parameter(description = "거절 상태 포함", example = "true", required = false)
    boolean rejected;

}
