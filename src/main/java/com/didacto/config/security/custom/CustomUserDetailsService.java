package com.didacto.config.security.custom;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.domain.Member;
import com.didacto.repository.member.MemberRepository;
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
                .map(this::createUserDetails)
                .orElseThrow(() -> new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL));
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private CustomUserDetails createUserDetails(Member member) {
        CustomUser Dto =  mapper.map(member, CustomUser.class);
        return new CustomUserDetails(Dto);
    }
}
