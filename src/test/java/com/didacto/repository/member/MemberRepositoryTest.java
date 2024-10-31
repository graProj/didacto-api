package com.didacto.repository.member;

import com.didacto.domain.Authority;
import com.didacto.domain.Grade;
import com.didacto.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Disabled
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("특정 이메일을 가진 회원을 조회한다.")
    @Test
    void findByEmail() {
        // given
        Member member1 = Member.builder()
                .email("member10@naver.com")
                .password("member10!!")
                .name("멤버10")
                .grade(Grade.Freeteer)
                .role(Authority.ROLE_USER)
                .build();

        Member member2 = Member.builder()
                .email("member20@naver.com")
                .password("member20!!")
                .name("멤버20")
                .grade(Grade.Freeteer)
                .role(Authority.ROLE_USER)
                .build();

        Member member3 = Member.builder()
                .email("member30@naver.com")
                .password("member30!!")
                .name("멤버30")
                .grade(Grade.Freeteer)
                .role(Authority.ROLE_USER)
                .build();

        memberRepository.saveAll(List.of(member1, member2, member3));

        // when
        Optional<Member> memberBYEmail2 = memberRepository.findByEmail("member20@naver.com");
        Optional<Member> memberBYEmail3 = memberRepository.findByEmail("member30@naver.com");

        List<Member> members = List.of(memberBYEmail2.get(), memberBYEmail3.get());

        // then
        assertThat(members).hasSize(2)
                .extracting("email", "name")
                .containsExactlyInAnyOrder(
                        tuple("member20@naver.com", "멤버20"),
                        tuple("member30@naver.com", "멤버30")
                );
    }

    @DisplayName("특정 이메일이 존재하는지 Boolean타입으로 반환한다.")
    @Test
    void existsByEmail() {
        // given
        Member member1 = Member.builder()
                .email("member10@naver.com")
                .password("member10!!")
                .name("멤버10")
                .grade(Grade.Freeteer)
                .role(Authority.ROLE_USER)
                .build();

        Member member2 = Member.builder()
                .email("member20@naver.com")
                .password("member20!!")
                .name("멤버20")
                .grade(Grade.Freeteer)
                .role(Authority.ROLE_USER)
                .build();

        memberRepository.saveAll(List.of(member1, member2));

        // when
        Boolean existMember1 = memberRepository.existsByEmail("member10@naver.com");
        Boolean existMember2 = memberRepository.existsByEmail("member30@naver.com");

        // then
        assertThat(existMember1).isTrue();
        assertThat(existMember2).isFalse();
    }
}