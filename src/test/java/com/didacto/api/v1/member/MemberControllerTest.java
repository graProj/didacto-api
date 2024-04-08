package com.didacto.api.v1.member;

import com.didacto.repository.member.MemberRepository;
import com.didacto.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DisplayName("Member Controller")
@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    MemberController memberController;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberService memberService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    //회원전체조회
    void findAllMembers() throws Exception {
        mockMvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk());
        verify(memberService).findAllMembers();
    }

    @Test
    //회원단건조회
    void findMember() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(get("/api/v1/members/{id}", id))
                .andExpect(status().isOk());
        verify(memberService).findMember(id);
    }
//
//    @Test
//    //회원정보수정
//    void editMemberInfo() {
//        // given
//        MemberEditRequestDto req = new MemberEditRequestDto("비밀번호수정","이름 수정");
//        Member member = createMember();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));
//
//        // when
//        mockMvc.perform(
//                put("/api/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req))
//        ).andExpect(status().isOk());
//
//        // then
//        verify(memberService).editMemberInfo(refEq(member), refEq(req));
//    }
//    }
//
//    @Test
//    //회원탈퇴
//    void deleteMemberInfo() {
//    }
}