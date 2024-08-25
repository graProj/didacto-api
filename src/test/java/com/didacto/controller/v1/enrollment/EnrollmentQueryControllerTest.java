package com.didacto.controller.v1.enrollment;

import com.didacto.dto.enrollment.EnrollmentResponse;
import com.didacto.service.enrollment.EnrollmentQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EnrollmentQueryController.class)
class EnrollmentQueryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EnrollmentQueryService enrollmentQueryService;

    @DisplayName("학생은 특정 ID의 초대 정보를 조회할 수 있다.")
    @WithMockUser(username = "STUDENT", roles = {"COMMON"})
    @Test
    void queryEnrollmentByIdFromStudent() throws Exception {

        //given
        EnrollmentResponse enrollment = new EnrollmentResponse();
        when(enrollmentQueryService.getEnrollmentById(1L)).thenReturn(enrollment);

        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/enrollment/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("강의 등록 요청 데이터를 조회하였습니다"));

    }

    @DisplayName("교수자는 특정 ID의 초대 정보를 조회할 수 있다.")
    @WithMockUser(username = "TUTOR", roles = {"ADMIN"})
    @Test
    void queryEnrollmentByIdFromAdmin() throws Exception {

        //given
        EnrollmentResponse enrollment = new EnrollmentResponse();
        when(enrollmentQueryService.getEnrollmentById(1L)).thenReturn(enrollment);

        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/enrollment/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("강의 등록 요청 데이터를 조회하였습니다"));

    }
}