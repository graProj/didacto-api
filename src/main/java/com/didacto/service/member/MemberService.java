package com.didacto.service.member;

import com.didacto.common.ErrorDefineCode;
import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberEditRequest;
import com.didacto.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void editMemberInfo(String userEmail, MemberEditRequest memberEditRequest) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> {
            throw new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL);
        });

        member.modify(
                passwordEncoder.encode(memberEditRequest.getPassword()),
                memberEditRequest.getName());
    }


    @Transactional
    public void deleteMember(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> {
            throw new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL);
        });
        memberRepository.delete(member);
    }
}

