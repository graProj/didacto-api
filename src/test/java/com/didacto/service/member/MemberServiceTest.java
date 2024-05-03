package com.didacto.service.member;

import com.didacto.domain.Member;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.dto.member.MemberResponse;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.didacto.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void queryAll() {
        // given
        Member member = createMember();
        Member member2 = createMember();
        List<Member> list = new LinkedList<>();
        list.add(member);
        list.add(member2);

        given(memberRepository.findAll()).willReturn(list);

        // when
        List<MemberResponse> result = memberService.queryAll();

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void query() {
        // given
        Long id = 5L;
        Member member = createMember();

        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        // when
        MemberResponse result = memberService.query(id);

        // then
        assertThat(result.getName()).isEqualTo("홍길동");
    }

    @Test
    void modifyInfo() {
        // given
        Long id = 5L;
        Member member = createMember();
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        MemberModificationRequest req = new MemberModificationRequest("dkfwhiba1230!@", "홍길자", "19990513");

        // when
        memberService.modifyInfo(member.getId(), req);

        // then
        assertThat(member.getName()).isEqualTo("홍길자");

    }





    @Test
    void delete() {
        //given
        Long id = 5L;
        Member member = createMember();
        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        //when
        memberService.delete(member.getId());

        //then
        assertTrue(member.getDeleted());

    }
}