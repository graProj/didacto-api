package com.didacto.controller.v1.enrollment;

import com.didacto.config.security.AuthConstant;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.EnrollmentStatus;
import com.didacto.dto.enrollment.EnrollmentConfirmRequest;
import com.didacto.dto.enrollment.EnrollmentRequest;
import com.didacto.service.enrollment.EnrollmentCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(controllers = EnrollmentCommandController.class)
@WithMockUser(username = "STUDENT", roles = {AuthConstant.USER})
class EnrollmentCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EnrollmentCommandService enrollmentService;
    @Autowired
    private ObjectMapper objectMapper;



    @DisplayName("학생은 강의 초대 요청을 보낼 수 있다.")
    @Test
    void sendRequest() throws Exception {

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

    @DisplayName("강의 초대 요청을 보낼 때 강의 ID는 필수값이다.")
    @Test
    void sendRequestWhenLectureIdNull() throws Exception {

        //given
        EnrollmentRequest request = new EnrollmentRequest(null);
        BDDMockito.given(enrollmentService.requestEnrollment(anyLong(), anyLong())).willReturn(1L);


        try(MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(2L);

            //when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/enrollment").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.name()))
                    .andExpect(jsonPath("$.message").value("Field Validation fail"));
        }
    }

    @DisplayName("학생은 초대 취소를 할 수 있다.")
    @Test
    void cancel() throws Exception {

        //given
        BDDMockito.given(enrollmentService.cancelEnrollment(anyLong(), anyLong())).willReturn(1L);


        try(MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(2L);

            //when, then
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/enrollment?enrollmentId=1").with(csrf()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("초대요청 취소를 할 때 초대 ID는 필수값이다.")
    @Test
    void cancelWhenEnrollmentIdNull() throws Exception {

        //given
        BDDMockito.given(enrollmentService.cancelEnrollment(anyLong(), anyLong())).willReturn(1L);


        try(MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(2L);

            //when, then
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/enrollment").with(csrf()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
        }
    }

    @DisplayName("교수자는 초대를 승인하거나 거절할 수 있다.")
    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, names = {"ACCEPTED", "REJECTED"})
    void apply(EnrollmentStatus status) throws Exception {

        //given
        EnrollmentConfirmRequest request = new EnrollmentConfirmRequest(1L, status.toString());
        BDDMockito.given(enrollmentService.confirmEnrollment(anyLong(), anyLong(), any(EnrollmentStatus.class))).willReturn(1L);


        try(MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(2L);

            //when, then
            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/enrollment").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("교수자는 승인 타입을 대기, 취소(WAITING, CANCELLED) 상태로 설정할 수 없다.")
    @ParameterizedTest
    @EnumSource(value = EnrollmentStatus.class, names = {"WAITING", "CANCELLED"})
    void applyWhenNotSupportedCommandType(EnrollmentStatus status) throws Exception {

        //given
        EnrollmentConfirmRequest request = new EnrollmentConfirmRequest(1L, status.toString());
        BDDMockito.given(enrollmentService.confirmEnrollment(anyLong(), anyLong(), any(EnrollmentStatus.class))).willReturn(1L);


        try(MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(2L);

            //when, then
            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/enrollment").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.name()))
                    .andExpect(jsonPath("$.message").value("Field Validation fail"));
        }
    }


    @DisplayName("교수자가 초대를 처리할 때 초대 ID는 필수값이다.")
    @Test
    void applyWhenEnrollmentIdNull() throws Exception {

        //given
        EnrollmentConfirmRequest request = new EnrollmentConfirmRequest(null, EnrollmentStatus.ACCEPTED.toString());
        BDDMockito.given(enrollmentService.confirmEnrollment(anyLong(), anyLong(), any(EnrollmentStatus.class))).willReturn(1L);


        try(MockedStatic<SecurityUtil> mSecurityUtil = mockStatic(SecurityUtil.class)) {
            BDDMockito.given(SecurityUtil.getCurrentMemberId()).willReturn(2L);

            //when, then
            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/enrollment").with(csrf())
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_ENTITY.name()))
                    .andExpect(jsonPath("$.message").value("Field Validation fail"));
        }
    }







}