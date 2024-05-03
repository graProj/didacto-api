package com.didacto.controller.v1.member;

import com.didacto.config.security.custom.CustomUser;
import com.didacto.config.security.custom.CustomUserDetails;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberModificationRequest;
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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.didacto.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        verify(memberService).queryAll();
    }

    @Test
        //회원단건조회
    void findMember() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(get("/api/v1/members/{id}", id))
                .andExpect(status().isOk());
        verify(memberService).query(id);
    }

    @Test
    //회원정보수정
    public void edidMember() throws Exception {
        // given
        MemberModificationRequest req = new MemberModificationRequest("dnjsaqksfd1230!@", "홍길삼", "19890221");
        Member member = createMember();
        CustomUser customUser = new CustomUser(member);
        CustomUserDetails userDetails = new CustomUserDetails(customUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        mockMvc.perform(
                put("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        );

        // then
        verify(memberService).modifyInfo(refEq(member.getId()), refEq(req));
    }


    @Test
    // 회원 탈퇴
    public void deleteMemberInfo() throws Exception {
        // given
        Member member = createMember();
        CustomUser customUser = new CustomUser(member); // CustomUser 생성
        CustomUserDetails userDetails = new CustomUserDetails(customUser); // CustomUserDetails 생성

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = member.getId(); // 회원의 ID를 얻어옴

        // when
        mockMvc.perform(delete("/api/v1/members"));

        // then
        verify(memberService).delete(userId);
    }



}