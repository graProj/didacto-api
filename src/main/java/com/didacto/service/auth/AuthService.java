package com.didacto.service.auth;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AlreadyExistElementException409;
import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.config.security.jwt.TokenProvider;
import com.didacto.config.security.custom.CustomUser;
import com.didacto.domain.Authority;
import com.didacto.domain.Member;
import com.didacto.dto.auth.*;
import com.didacto.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public Long signup(SignUpRequest req) {
        validateSignUpInfo(req);
        Member member = createSignupFormOfUser(req);
        Member result = memberRepository.save(member);
        return result.getId();
    }

    @Transactional
    public TokenResponse signIn(LoginRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(() -> {
            throw new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL);
        });
        validatePassword(req, member);
        TokenDto token = generateToken(member);
        return new TokenResponse(token.getAccessToken(), token.getRefreshToken());
    }


    @Transactional
    public TokenResponse reissueAccessToken(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL);
        });
        TokenDto token = generateToken(member);
        return new TokenResponse(token.getAccessToken(), null);
    }

    private Member createSignupFormOfUser(SignUpRequest req) {
        Member member = Member.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .role(Authority.ROLE_USER)
                .build();
        return member;
    }


    private void validateSignUpInfo(SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new AlreadyExistElementException409(ErrorDefineCode.ALREADY_EXIST_EMAIL);
        }
    }

    private void validatePassword(LoginRequest loginRequest, Member member) {
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new AuthCredientialException401(ErrorDefineCode.AUTH_NMATCH_PWD);
        }
    }



    private TokenDto generateToken(Member member){
        CustomUser customUser = new CustomUser(member.getId(), member.getEmail(), member.getPassword(), member.getRole());
        TokenDto token = tokenProvider.generateTokenDto(customUser);
        return token;
    }

}
