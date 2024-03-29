package com.didacto.api.v1;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.response.CommonResponse;
import com.didacto.common.response.SwaggerErrorResponseType;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.sign.LoginRequestDto;
import com.didacto.dto.sign.SignUpRequestDto;
import com.didacto.dto.sign.TokenRequestDto;
import com.didacto.dto.sign.TokenResponseDto;
import com.didacto.service.AuthService;

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


    @PostMapping("/sign-up")
    @Operation(summary = "회원가입 API", description = "회원가입을 시킨다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "중복된 이메일",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })
    public CommonResponse register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return new CommonResponse<>(true, HttpStatus.CREATED, "회원 가입에 성공했습니다.",signUpRequestDto.getEmail());
    }


    @PostMapping("/sign-in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "사용자 인증 실패",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })
    @Operation(summary = "로그인 API", description = "로그인을 시킨다.")
    public CommonResponse<TokenResponseDto> signIn(@Valid @RequestBody LoginRequestDto req) {
        TokenResponseDto token = authService.signIn(req);
        return new CommonResponse<>(true, HttpStatus.OK, "로그인에 성공했습니다", token);
    }


    @PostMapping("/refresh")
    @PreAuthorize(AuthConstant.AUTH_REFRESH)
    @Operation(summary = "Access Token 재발급", description = "토큰 재발급 : Bearer에 Refresh Token 넣어서 요청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "유효하지 않은 Refresh Token",
                    content = {@Content(schema = @Schema(implementation = SwaggerErrorResponseType.class))})
    })
    public CommonResponse reissue() {
        String email = SecurityUtil.getCurrentMemberEmail();
        String token = authService.generateAccessToken(email);
        return new CommonResponse<>(true, HttpStatus.OK, "토큰 재발급에 성공했습니다.", token);
    }

}
