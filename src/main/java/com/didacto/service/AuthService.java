package com.didacto.service;

import com.didacto.config.exception.exception.EmailAlreadyExistsException;
import com.didacto.config.exception.exception.LoginFailureException;
import com.didacto.config.jwt.TokenProvider;
import com.didacto.config.security.CustomUserDto;
import com.didacto.domain.Authority;
import com.didacto.domain.Member;
import com.didacto.domain.RefreshToken;
import com.didacto.dto.sign.*;
import com.didacto.repository.MemberRepository;
import com.didacto.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignUpRequestDto req) {
        validateSignUpInfo(req);
        Member member = createSignupFormOfUser(req);
        memberRepository.save(member);
    }

    @Transactional
    public TokenResponseDto signIn(LoginRequestDto req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(() -> {
            throw new LoginFailureException();
        });

        validatePassword(req, member);
//        Authentication authentication = getUserAuthentication(req);
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//        RefreshToken refreshToken = buildRefreshToken(authentication, tokenDto);
//        refreshTokenRepository.save(refreshToken);
//        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        CustomUserDto customUserDto = new CustomUserDto(member.getId(), member.getEmail(), member.getPassword(), member.getAuthority());
        TokenDto tokenDto = tokenProvider.generateTokenDto(customUserDto);

//        RefreshToken refreshToken = buildRefreshToken(member.getEmail(), tokenDto);
//        refreshTokenRepository.save(refreshToken);
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    private RefreshToken buildRefreshToken(String email, TokenDto tokenDto) {
        return RefreshToken.builder()
                .key(email)
                .value(tokenDto.getRefreshToken())
                .build();
    }

    private Authentication getUserAuthentication(LoginRequestDto req) {
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return authentication;
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        validateRefreshToken(tokenRequestDto);

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        validateRefreshTokenOwner(refreshToken, tokenRequestDto);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return tokenResponseDto;
    }

    private Member createSignupFormOfUser(SignUpRequestDto req) {
        Member member = Member.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .authority(Authority.ROLE_USER)
                .build();
        return member;
    }


    private void validateSignUpInfo(SignUpRequestDto signUpRequestDto) {
        if (memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException(signUpRequestDto.getEmail());
        }
    }

    private void validatePassword(LoginRequestDto loginRequestDto, Member member) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private void validateRefreshToken(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
    }

    private void validateRefreshTokenOwner(RefreshToken refreshToken, TokenRequestDto tokenRequestDto) {
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}