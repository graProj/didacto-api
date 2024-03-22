package com.didacto.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.util.validation.ValidationError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Schema(title = "Common response : Error 필드")
@RequiredArgsConstructor
public class CommonError {

    @Schema(description = "개발자 정의 에러코드", example = "ERR01")
    private final String errorCode;

    @Schema(description = "시간")
    private final LocalDateTime time;

    @Schema(description = "Stacktrace(라이브 시 안보이게)")
    private String stackTrace;

    @Schema(description = "Validation Error시 발생된 Field List")
    private List<ValidationError> errors;


    @Data
    @RequiredArgsConstructor
    @Schema(title = "Common response : Validation 오류 필드")
    private static class ValidationError {

        @Schema(description = "Validation 에러 발생 필드", example = "name")
        private final String field;
        @Schema(description = "Validation 오류 내", example = "비밀번호는 영문과 특문을 포함해야 합니다.")
        private final String message;
    }

    public void addValidationError(String field, String message){
        if(Objects.isNull(errors)){
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }
}
