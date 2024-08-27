package com.didacto.service.member;

import com.didacto.domain.Authority;
import com.didacto.domain.Grade;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.dto.member.MemberResponse;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


//@AutoConfigureTestDatabase를 사용해야 Test용 DB를 사용함
//@ExtenWith를 사용해야 Mokito를 사용할 수 있음

@Disabled
@AutoConfigureTestDatabase
@SpringBootTest
class MemberServiceTest {


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @DisplayName("전체회원을 조회한다.")
    @Test
    void queryAll() {
        // given
        Member member1 = CreateMember(1L,"gildong1@naver.com","gildong123!@","회원1");
        Member member2 = CreateMember(2L, "gildong2@naver.com","gildong456!@","회원2");
        memberRepository.saveAll(List.of(member1, member2));


        // when
        List<MemberResponse> result = memberService.queryAll();

        // then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result).hasSize(2)
                .extracting("email", "name")
                .containsExactlyInAnyOrder(
                        tuple("gildong1@naver.com", "회원1"),
                        tuple("gildong2@naver.com", "회원2")
                );
    }

    @DisplayName("특정 id에 해당하는 회원을 찾는다.")
    @Test
    void query() {
        // given
        Member member1 = CreateMember(1L,"gildong1@naver.com","gildong123!@","회원1");
        Member member2 = CreateMember(2L,"gildong2@naver.com","gildong456!@","회원2");
        Member member3 = CreateMember(3L, "gildong3@naver.com","gildong789!@","회원3");

        memberRepository.saveAll(List.of(member1, member2, member3));

        // when
        MemberResponse result = memberService.query(1L);

        // then
        assertThat(result)
                .extracting("id", "email", "name")
                .contains(1L, "gildong1@naver.com", "회원1");
    }

    @DisplayName("회원의 정보중 비밀번호, 이름, 생년월일을 수정할 수 있다.")
    @Test
    void modifyInfo() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .email("gildong1@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .birth(memberService.parseBirth("20000513"))
                .build();
        memberRepository.save(member1);

        MemberModificationRequest req = new MemberModificationRequest("gildong456!@", "회원2", "19990513");

        // when
        memberService.modifyInfo(member1.getId(), req);

        // then
        Optional<Member> modifyMember = memberRepository.findByEmail("gildong1@naver.com");

        assertThat(modifyMember.get())
                .extracting("name", "birth")
                .contains("회원2", memberService.parseBirth(String.valueOf(19990513)));

    }





    @Test
    void delete() {
        //given
        Member member1 = CreateMember(1L,"gildong1@naver.com","gildong123!@","회원1");
        Member member2 = CreateMember(2L,"gildong2@naver.com","gildong456!@","회원2");
        Member member3 = CreateMember(3L, "gildong3@naver.com","gildong789!@","회원3");
        memberRepository.saveAll(List.of(member1,member2,member3));

        //when
        memberService.delete(member3.getId());

        //then
        Optional<Member> modifyMember = memberRepository.findByEmail("gildong3@naver.com");
        assertThat(modifyMember.get().getDeleted()).isTrue();

    }


    private Member CreateMember(Long id, String email, String password, String name){
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .build();
    }

}