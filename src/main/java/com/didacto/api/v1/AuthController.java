package com.didacto.api.v1;

import com.didacto.common.response.CommonResponse;
import com.didacto.dto.sign.LoginRequestDto;
import com.didacto.dto.sign.SignUpRequestDto;
import com.didacto.dto.sign.TokenRequestDto;
import com.didacto.dto.sign.TokenResponseDto;
import com.didacto.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public CommonResponse register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return new CommonResponse<>(true, HttpStatus.CREATED, "회원 가입에 성공했습니다.", null);
    }

    @PostMapping("/sign-in")
    public CommonResponse<TokenResponseDto> signIn(@Valid @RequestBody LoginRequestDto req) {
        TokenResponseDto token = authService.signIn(req);
        return new CommonResponse<>(true, HttpStatus.OK, "로그인에 성공했습니다", token);
    }
}
