package com.didacto;

import com.didacto.domain.Member;
import com.didacto.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static com.didacto.MemberFactory.createMember;
@DataJpaTest
public class MemberTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버가 DB에 저장이 잘 되는지 확인")
    void saveMember() {
        // given
        Member member = createMember();
        // when
        Member savedMember = memberRepository.save(member);
        // then
        Assertions.assertThat(member).isSameAs(savedMember);
        Assertions.assertThat(member.getName()).isEqualTo(savedMember.getName());
        Assertions.assertThat(savedMember.getId()).isNotNull();
        Assertions.assertThat(memberRepository.count()).isEqualTo(1);
    }
}