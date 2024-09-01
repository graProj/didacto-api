package com.didacto.controller.v1.member;
import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.config.security.custom.CustomUserDetailsService;
import com.didacto.dto.member.MemberModificationRequest;
import com.didacto.dto.member.MemberResponse;

import com.didacto.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    @DisplayName("전체회원을 조회한다.")
    @Test
    void findAllMembers() throws Exception {
        //given
        List<MemberResponse> result = List.of();
        given(memberService.queryAll()).willReturn(result);

        //when, then
        mockMvc.perform(get("/api/v1/members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 조회에 성공했습니다."))
                .andExpect(jsonPath("$.response").isArray());

    }


    @DisplayName("특정 id로 한명의 회원을 조회한다.")
    @Test
    void findMember() throws Exception {
        //given
        MemberResponse result = new MemberResponse();
        given(memberService.query(1L)).willReturn(result);

        //when, then
        mockMvc.perform(get("/api/v1/members/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 조회에 성공했습니다."));
    }


    @DisplayName("회원의 정보중 비밀번호, 이름, 생년월일 수정을 할 수 있다.")
    @Test
    @WithMockUser(username = "user", roles = {AuthConstant.AUTH_ALL})
    //회원정보수정
    public void edidMember() throws Exception {

        // given

        MemberModificationRequest req = new MemberModificationRequest("gildong456!@", "회원2", "19990513");
        BDDMockito.willDoNothing().given(memberService).modifyInfo(anyLong(), ArgumentMatchers.eq(req));

        try (MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(1L);
            // when, then
            mockMvc.perform(
                            put("/api/v1/members")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(req))

                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }


    @DisplayName("회원을 삭제할 수 있다.(soft-delete)")
    @Test
    @WithMockUser(username = "user", roles = {AuthConstant.AUTH_ALL})
    // 회원 탈퇴
    public void deleteMemberInfo() throws Exception {
        // given
        BDDMockito.willDoNothing().given(memberService).delete(anyLong());

        try (MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(1L);
            // when, then
            mockMvc.perform(
                            delete("/api/v1/members")
                                    .with(csrf())
                                    .contentType(APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }



}