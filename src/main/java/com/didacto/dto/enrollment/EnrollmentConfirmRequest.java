package com.didacto.dto.enrollment;


import com.didacto.domain.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Enrollemnt : 참여요청 상태 변경 스키마")
public class EnrollmentConfirmRequest {

    @Schema(example = "1", required = true, description = "에러 유형 (500, 404, 403, 415), 그 외 정상 ")
    private Long enrollmentId;

    @NotBlank(message = "참여요청 기록의 ID가 입력되지 않았습니다.")
    @Pattern(regexp = "^(ACCEPTED|REJECTED)$", message = "동작은 승인(ACCEPTED) 혹은 거절(REJECTED) 둘 중 하나여야 합니다.")
    @Schema(example = "ACCEPTED || REJECTED", description = "에러 유형 (500, 404, 403, 415), 그 외 정상 ")
    private String action;

}
