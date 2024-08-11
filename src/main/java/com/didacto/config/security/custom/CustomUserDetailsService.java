package com.didacto.config.security.custom;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.domain.Authority;
import com.didacto.domain.Member;
import com.didacto.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .filter(member -> !member.getDeleted())
                .map(this::createUserDetails)
                .orElseThrow(() -> new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL));
    }

    public UserDetails loadUserDetailsByClaim(Claims claim) throws UsernameNotFoundException {
        CustomUser dto = new CustomUser(claim.get("Id", Long.class), claim.getSubject(), null, null);
        return new CustomUserDetails(dto);
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private CustomUserDetails createUserDetails(Member member) {
        CustomUser dto = new CustomUser(member.getId(), member.getPassword(), member.getEmail(), member.getRole());
        return new CustomUserDetails(dto);
    }
}
