package com.didacto.dto.enrollment;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Enrollemnt : 강의 등록 요청")
public class EnrollmentRequest {

    @NotNull(message = "강의 ID가 입력되지 않았습니다.")
    @Schema(example = "1")
    private Long lectureId;

}
