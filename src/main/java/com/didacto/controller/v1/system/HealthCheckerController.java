package com.didacto.controller.v1.system;

import com.didacto.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/health-check")
@Tag(name = "HEALTH_CHECKER", description = "헬스 체커")
public class HealthCheckerController {

    @GetMapping("")
    public CommonResponse<String> health() {
        return new CommonResponse(
                true, HttpStatus.OK, "API Online", "API Online"
        );
        
    }

}
