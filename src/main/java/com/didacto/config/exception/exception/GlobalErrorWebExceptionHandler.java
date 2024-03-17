package com.didacto.config.exception.exception;

import com.didacto.config.exception.response.CommonResponse;
import com.didacto.config.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

//@Controller
//@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorController {

    private final ErrorAttributes errorAttributes;

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }

    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<CommonResponse<Void>> handleError(HttpServletRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        ErrorResponse errorResponse = new ErrorResponse(
                (String) errorPropertiesMap.get("error"),
                ErrorCode.BAD_REQUEST.getCode(),
                (Integer) errorPropertiesMap.get("status")
        );
        CommonResponse<Void> response = new CommonResponse<>(false, null, errorResponse);
        return ResponseEntity.status((Integer) errorPropertiesMap.get("status"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // 다른 예외들에 대한 처리를 추가할 수 있습니다.
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<CommonResponse<Void>> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ErrorCode.UNKNOWN.getCode(),
                ErrorCode.UNKNOWN.getStatus().value()
        );
        CommonResponse<Void> response = new CommonResponse<>(false, null, errorResponse);
        return ResponseEntity.status(ErrorCode.UNKNOWN.getStatus()).body(response);
    }
}
