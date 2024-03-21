package com.didacto.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Schema(title = "Common response : Error 필드")
@RequiredArgsConstructor
public class CommonError {

    private final String errorCode;
    private final LocalDateTime time;
    private String stackTrace;
    private List<ValidationError> errors;


    @Data
    @RequiredArgsConstructor
    @Schema(title = "Common response : Validation 오류 필드")
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message){
        if(Objects.isNull(errors)){
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }
}
