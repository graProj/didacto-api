package com.didacto.service;

import com.didacto.config.exception.exception.EmailAlreadyExistsException;
import com.didacto.config.exception.exception.LoginFailureException;
import com.didacto.config.jwt.TokenProvider;
import com.didacto.config.security.CustomUserDto;
import com.didacto.domain.Authority;
import com.didacto.domain.Member;
import com.didacto.dto.sign.*;
import com.didacto.repository.MemberRepository;
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
        CustomUserDto customUserDto = new CustomUserDto(member.getId(), member.getEmail(), member.getPassword(), member.getAuthority());
        TokenDto tokenDto = tokenProvider.generateTokenDto(customUserDto);

        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    private Authentication getUserAuthentication(LoginRequestDto req) {
        UsernamePasswordAuthenticationToken authenticationToken = req.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return authentication;
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

}
