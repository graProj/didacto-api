package com.didacto.service.member;

import com.didacto.domain.Authority;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.dto.member.MemberResponse;
import com.didacto.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.didacto.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


//@AutoConfigureTestDatabase를 사용해야 Test용 DB를 사용함
//@ExtenWith를 사용해야 Mokito를 사용할 수 있음
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    //InjectMocks : 생성된 Mock 객체를 사용하고 있는 객체에게 주입한다는 어노테이션
    //Mock : Mock 객체를 자동적으로 생성해주는 어노테이션

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void queryAll() {
        // given
        Member member1 = createMember(1L,"gildong456@naver.com","홍길동","gildong123456!@","19960129", Authority.ROLE_USER);
        Member member2 = createMember(2L,"gilsam456@naver.com","홍길삼","gilsam123456!@","19960130", Authority.ROLE_USER);
        List<Member> list = new LinkedList<>();
        list.add(member1);
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
        Member member = createMember(1L,"gildong456@naver.com","홍길동","gildong123456!@","19960129", Authority.ROLE_USER);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        // when
        MemberResponse result = memberService.query(member.getId());

        // then
        assertThat(result.getName()).isEqualTo("홍길동");
    }

    @Test
    void modifyInfo() {
        // given
        Member member = createMember(1L,"gildong456@naver.com","홍길동","gildong123456!@","19960129", Authority.ROLE_USER);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        MemberModificationRequest req = new MemberModificationRequest("dkfwhiba1230!@", "홍길자", "19990513");

        // when
        memberService.modifyInfo(member.getId(), req);

        // then
        assertThat(member.getName()).isEqualTo("홍길자");

    }





    @Test
    void delete() {
        //given
        Member member = createMember(1L,"gildong456@naver.com","홍길동","gildong123456!@","19960129", Authority.ROLE_USER);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        //when
        memberService.delete(member.getId());

        //then
        assertTrue(member.getDeleted());

    }
}