package com.didacto.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Schema(title = "Exception response : 예외 타입 응답")
public class SwaggerErrorResponseType {
    @Schema(description = "성공 여부", example = "false")
    private final boolean success;

    @Schema(description = "HTTP Status", example = "Unauthoirzation, Forbidden..")
    private final HttpStatus status;

    @Schema(description = "응답 메시지", example = "~가 실패했습니다")
    private final String message;

    @Schema(description = "응답 데이터")
    private final CommonError response;
}
