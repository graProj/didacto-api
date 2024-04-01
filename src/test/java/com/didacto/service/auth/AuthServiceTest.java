package com.didacto.service.auth;

import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.config.security.jwt.TokenProvider;
import com.didacto.dto.auth.LoginRequest;
import com.didacto.dto.auth.SignUpRequest;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.didacto.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    AuthService authService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenProvider tokenProvider;



    @BeforeEach
    void beforeEach() {
        authService = new AuthService(memberRepository, passwordEncoder, tokenProvider);
    }

//    @Test
//    void 회원가입_테스트() {
//        // given
//        SignUpRequest req = new SignUpRequest("gildong@naver.com", "gildong123!!","홍길동","20000621","USER");
//
//        // when
//        authService.signup(req);
//
//        // then
//        verify(passwordEncoder).encode(req.getPassword());
//        verify(memberRepository).save(any());
//    }

    @Test
    void 로그인실패_테스트() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequest("email", "password")))
                .isInstanceOf(AuthCredientialException401.class);
    }

    @Test
    void 비밀번호_검증_테스트() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> authService.signIn(new LoginRequest("username", "password")))
                .isInstanceOf(AuthCredientialException401.class);
    }

}