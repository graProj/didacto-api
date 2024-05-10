package com.didacto.controller.v1.auth;

import com.didacto.common.response.CommonResponse;
import com.didacto.common.response.SwaggerErrorResponseType;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.auth.LoginRequest;
import com.didacto.dto.auth.SignUpRequest;
import com.didacto.dto.auth.TokenResponse;
import com.didacto.service.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Tag(name = "AUTH API", description = "로그인, 회원가입과 관련된 API") // Swagger Docs : API 이름
@RequiredArgsConstructor
@RestController

@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;



    @PostMapping("/signup")
    @Operation(summary = "AUTH_01 : 회원가입 API", description = "회원가입을 시킨다.")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "409", description = "중복된 이메일",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })

    public CommonResponse<Long> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long result = authService.signup(signUpRequest);
        return new CommonResponse<>(true, HttpStatus.CREATED, "회원 가입에 성공했습니다.", result);
    }


    @PostMapping("/signin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "사용자 인증 실패",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })
    @Operation(summary = "AUTH_02 : 로그인 API", description = "로그인을 시킨다.")
    public CommonResponse<TokenResponse> signIn(@Valid @RequestBody LoginRequest req) {
        TokenResponse token = authService.signIn(req);
        return new CommonResponse<>(true, HttpStatus.OK, "로그인에 성공했습니다", token);
    }



    @PostMapping("/refresh")
    @PreAuthorize(AuthConstant.REFRESH)
    @Operation(summary = "AUTH_03 : Access Token 재발급", description = "토큰 재발급 : Bearer에 Refresh Token 넣어서 요청 \\\n" +
            "curl -X 'POST' \\\n" +
            "  'http://localhost:8080/api/v1/auth/refresh' \\\n" +
            "  -H 'accept: application/json' \\\n" +
            "  -H 'Authorization: Bearer YOUR_REFRESH_TOKEN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Refresh Token이 없거나 유효하지 않음",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })
    public CommonResponse<TokenResponse> reissue() {
        Long id = SecurityUtil.getCurrentMemberId();
        TokenResponse token = authService.reissueAccessToken(id);
        return new CommonResponse<>(true, HttpStatus.OK, "Access 토큰 재발급에 성공했습니다.", token);
    }
}
