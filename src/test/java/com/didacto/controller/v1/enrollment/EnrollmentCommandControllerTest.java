package com.didacto.controller.v1.enrollment;

import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.service.enrollment.EnrollmentCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(controllers = EnrollmentCommandController.class)
class EnrollmentCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EnrollmentCommandService enrollmentService;
    @Autowired
    private ObjectMapper objectMapper;



    @DisplayName("학생은 강의 초대 요청을 보낼 수 있다.")
    @WithMockUser(username = "STUDENT", roles = {AuthConstant.USER})
    @Test
    void createRequest() throws Exception {

        //given
        EnrollmentRequest request = new EnrollmentRequest(5L);
        BDDMockito.given(enrollmentService.requestEnrollment(anyLong(), anyLong())).willReturn(1L);


        try(MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(2L);

            //when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/enrollment").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
        }
    }



}