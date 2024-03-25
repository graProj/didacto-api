package com.didacto.api.v1;

import com.didacto.config.exception.response.CommonResponse;
import com.didacto.dto.sign.LoginRequestDto;
import com.didacto.dto.sign.SignUpRequestDto;
import com.didacto.dto.sign.TokenRequestDto;
import com.didacto.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public CommonResponse register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return new CommonResponse<>(true, true, null);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public CommonResponse signIn(@Valid @RequestBody LoginRequestDto req) {
        return new CommonResponse<>(true, authService.signIn(req), null);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public CommonResponse reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return new CommonResponse<>(true, authService.reissue(tokenRequestDto), null);
    }
}
