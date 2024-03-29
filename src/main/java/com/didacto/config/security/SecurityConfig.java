package com.didacto.config.security;


import com.didacto.config.jwt.JwtAccessDeniedHandler;
import com.didacto.config.jwt.JwtAuthenticationEntryPoint;
import com.didacto.config.jwt.JwtSecurityConfig;
import com.didacto.config.jwt.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
//EnableMethodSecurity를 통해 메소드 단위 보안 수준 설정 가능

//TODO :
//
// 5. PreAuthorization 어노테이션을 통해 컨트롤러 메서드 단위의 인가를 수행할 수 있도록 설정한다.
// 6. 인가 수행 시 Role(권한)에 따라서 수행 권한을 제한하거나 분리할 수 있도록 설정해야 한다.
// 7. 컨트롤러 단에서 현재 인증 세션 내에 있는 JWT의 User PK를 추출하여 사용할 수 있도록 하는 유틸 클래스를 작성한다.
// 8. access토큰 refresh 토큰 재발급
// 9. 인증 및 인가 실패에 대한 Exception Handler를 정의한다.
// 10. 해당 과정이 끝난 후 변경된 코드 컨벤션에 따라서 리팩토링한다.


public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF, CORS
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.headers(
                headersConfigurer -> headersConfigurer
                        .frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
        );
       http.exceptionHandling(handler -> handler.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler))

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        //FormLogin, BasicHttp 비활성화
        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);




        //TODO : JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                //TODO : 권한 규칙 작성
        .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().permitAll()
        );



        // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
        http.with(new JwtSecurityConfig(tokenProvider), customizer -> {});



        return http.build();
    }


}