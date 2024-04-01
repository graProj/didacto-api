package com.didacto.api.v1;


import com.didacto.dto.auth.LoginRequest;
import com.didacto.dto.auth.SignUpRequest;
import com.didacto.dto.auth.TokenResponse;
import com.didacto.service.auth.AuthService;
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
import org.springframework.http.MediaType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DisplayName("Auth Controller")
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    AuthController authController;
    @Mock
    AuthService authService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void 회원가입_테스트() throws Exception{
        //given
        SignUpRequest req = new SignUpRequest("gildong@naver.com", "gildong123!!","홍길동","20000621","USER");

        //when
        mockMvc.perform(
                post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isCreated());

        //then
        verify(authService).signup(req);
    }

    @Test
    public void 로그인_테스트() throws Exception {
        // given
        LoginRequest req = new LoginRequest("test123", "test");
        given(authService.signIn(req)).willReturn(new TokenResponse("access", "refresh"));

        // when, then
        mockMvc.perform(
                        post("/api/v1/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.accessToken").value("access"))
                .andExpect(jsonPath("$.response.refreshToken").value("refresh"));

        verify(authService).signIn(req);
    }
}