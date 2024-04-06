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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<MemberFindResponse> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberFindResponse::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberFindResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberFindResponse.toDto(member);
    }

    @Transactional
    public void editMemberInfo(String userEmail, MemberEditRequest memberEditRequest) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> {
            throw new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL);
        });

        member.modify(
                passwordEncoder.encode(memberEditRequest.getPassword()),
                memberEditRequest.getName(),
                parseBirth(memberEditRequest.getBirth()));
    }

    private OffsetDateTime parseBirth(String birth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birth, formatter);
        return birthDate.atStartOfDay().atOffset(ZoneOffset.UTC);
    }


    @Transactional
    public void deleteMember(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> {
            throw new AuthCredientialException401(ErrorDefineCode.AUTH_NOT_FOUND_EMAIL);
        });
        memberRepository.delete(member);
    }
}

