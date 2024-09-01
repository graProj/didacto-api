package com.didacto.service.member;

import com.didacto.common.ErrorDefineCode;
import com.didacto.common.util.DateUtil;
import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.dto.member.MemberResponse;
import com.didacto.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberQueryService memberQueryService;
    private final DateUtil dateUtil;

    @Transactional(readOnly = true)
    public List<MemberResponse> queryAll() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .filter(member -> !member.getDeleted())
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberResponse query(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementFoundException404(ErrorDefineCode.MEMBER_NOT_FOUND);
        });

        if(!member.getDeleted()){
            return new MemberResponse(member);
        }else{
            throw new AuthCredientialException401(ErrorDefineCode.MEMBER_UNRESISTER);
        }
    }






    public void modifyInfo(Long userId, MemberModificationRequest memberEditRequest) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> {
            throw new NoSuchElementFoundException404(ErrorDefineCode.MEMBER_NOT_FOUND);
        });

        if(!member.getDeleted()){
            member.modify(
                    passwordEncoder.encode(memberEditRequest.getPassword()),
                    memberEditRequest.getName(),
                    dateUtil.parseBirth(memberEditRequest.getBirth()));
            memberRepository.save(member);
        }else{
            throw new AuthCredientialException401(ErrorDefineCode.MEMBER_UNRESISTER);
        }

    }



    public void delete(Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> {
            throw new NoSuchElementFoundException404(ErrorDefineCode.MEMBER_NOT_FOUND);
        });
        member.delete();
        memberRepository.save(member);
    }
}

