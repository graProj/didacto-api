package com.didacto.service.member;

import com.didacto.config.exception.custom.exception.AuthCredientialException401;
import com.didacto.config.exception.custom.exception.NoSuchElementFoundException404;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.dto.member.MemberResponse;
import com.didacto.repository.member.MemberRepository;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;



    @DisplayName("전체회원을 조회한다.")
    @Test
    void queryAll() {
        // given
        Member member1 = CreateMember("gildong1@naver.com","gildong123!@","회원1", false);
        Member member2 = CreateMember( "gildong2@naver.com","gildong456!@","회원2", false);
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

    @DisplayName("전체회원을 조회할 때 탈퇴한 회원은 제외하고 계산한다.")
    @Test
    void queryAllExcept_Deleted() {
        // given
        Member member1 = CreateMember("gildong1@naver.com","gildong123!@","회원1", false);
        Member member2 = CreateMember( "gildong2@naver.com","gildong456!@","회원2", false);
        Member member3 = CreateMember( "gildong3@naver.com","gildong789!@","회원3", false);
        memberRepository.saveAll(List.of(member1, member2, member3));

        member3.delete();

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
        Member member1 = CreateMember("gildong1@naver.com","gildong123!@","회원1", false);
        Member member2 = CreateMember("gildong2@naver.com","gildong456!@","회원2", false);
        Member member3 = CreateMember( "gildong3@naver.com","gildong789!@","회원3", false);
        memberRepository.saveAll(List.of(member1, member2, member3));

        // when
        MemberResponse result = memberService.query(member1.getId());

        // then
        assertThat(result)
                .extracting("email", "name")
                .contains("gildong1@naver.com", "회원1");
    }

    @DisplayName("특정 id에 해당하는 회원을 조회할 때 회원이 없다면 예외를 터뜨린다.")
    @Test
    void queryNotFound() {
        // given
        Member member1 = CreateMember("gildong1@naver.com","gildong123!@","회원1",false);
        Member member2 = CreateMember("gildong2@naver.com","gildong456!@","회원2", false);
        Member member3 = CreateMember( "gildong3@naver.com","gildong789!@","회원3", false);

        memberRepository.saveAll(List.of(member1, member2));

        Long nonExistentId = -1L;

        // when then
        assertThatThrownBy(() -> memberService.query(nonExistentId))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("회원을 찾을 수 없습니다.");
    }

    @DisplayName("특정 id에 해당하는 회원을 조회할 때 탈퇴한 상태(soft-deleted)라면 예외를 터뜨린다.")
    @Test
    void queryMember_Deleted() {
        // given
        Member member1 = Member.builder()
                .email("gildong@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .deleted(true)
                .build();

        member1.delete();
        memberRepository.save(member1);

        // when then
        assertThatThrownBy(() -> memberService.query(member1.getId()))
                .isInstanceOf(AuthCredientialException401.class)
                .hasMessage("탈퇴된 회원입니다.");
    }

    @DisplayName("회원의 정보중 비밀번호, 이름, 생년월일을 수정할 수 있다.")
    @Test
    void modifyInfo() {
        // given
        Member member1 = Member.builder()
                .email("gildong1@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .deleted(false)
                .birth(OffsetDateTime.of(2000, 5, 13, 0, 0, 0, 0, ZoneOffset.UTC))
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
    @DisplayName("회원의 정보를 수정하려고 할 때 회원을 찾을 수 없다면 예외를 터뜨린다.")
    @Test
    void modifyInfoNotFound() {
        // given
        Member member1 = Member.builder()
                .email("gildong@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .birth(memberService.parseBirth("20000513"))
                .build();
        Member member2 = Member.builder()
                .email("gildong2@naver.com")
                .password("gildong456!@")
                .name("회원2")
                .birth(memberService.parseBirth("20000520"))
                .build();

        memberRepository.save(member1);

        MemberModificationRequest req = new MemberModificationRequest("gildong456!@", "회원2", "19990513");
        Long nonExistentId = -1L;
        // when, then

        assertThatThrownBy(() -> memberService.modifyInfo(nonExistentId, req))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("회원을 찾을 수 없습니다.");

    }


    @DisplayName("회원의 정보를 수정하려고 할 때 탈퇴한 상태(soft-deleted)라면 예외를 터뜨린다.")
    @Test
    void modifyInfo_deleted() {
        // given
        Member member1 = Member.builder()
                .email("gildong@naver.com")
                .password("gildong123!@")
                .name("회원1")
                .birth(memberService.parseBirth("20000513"))
                .deleted(true)
                .build();

        member1.delete();
        memberRepository.save(member1);



        MemberModificationRequest req = new MemberModificationRequest("gildong456!@", "회원2", "19990513");

        // when, then
        assertThatThrownBy(() -> memberService.modifyInfo(member1.getId(), req))
                .isInstanceOf(AuthCredientialException401.class)
                .hasMessage("탈퇴된 회원입니다.");

    }



    @DisplayName("회원을 탈퇴시키면 deleted컬럼이 false에서 true로 바뀐다.")
    @Test
    void delete() {
        //given
        Member member1 = CreateMember("gildong1@naver.com","gildong123!@","회원1",false);
        Member member2 = CreateMember("gildong2@naver.com","gildong456!@","회원2", false);
        Member member3 = CreateMember( "gildong3@naver.com","gildong789!@","회원3", false);
        memberRepository.saveAll(List.of(member1,member2,member3));

        //when
        memberService.delete(member3.getId());
        //then
        Optional<Member> modifyMember = memberRepository.findByEmail("gildong3@naver.com");
        assertThat(modifyMember.get().getDeleted()).isTrue();

    }

    @DisplayName("회원을 탈퇴시키려하는데 회원을 찾을 수 없다면 예외를 터뜨리다.")
    @Test
    void deleteNotFound() {
        //given
        Member member1 = CreateMember("gildong1@naver.com","gildong123!@","회원1",false);
        Member member2 = CreateMember("gildong2@naver.com","gildong456!@","회원2", false);
        Member member3 = CreateMember("gildong3@naver.com","gildong789!@","회원3", false);
        memberRepository.saveAll(List.of(member1,member2));

        Long nonExistentId = -1L;
        //when, then
        assertThatThrownBy(() -> memberService.delete(nonExistentId))
                .isInstanceOf(NoSuchElementFoundException404.class)
                .hasMessage("회원을 찾을 수 없습니다.");

    }


    private Member CreateMember(String email, String password, String name, Boolean deleted){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .deleted(deleted)
                .build();
    }

}