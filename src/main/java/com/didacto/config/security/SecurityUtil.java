package com.didacto.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// 컨트롤러 단에서 JWT의 User Pk를 추출하여 사용할 수 있또록 하는 유틸 클래스
@Slf4j
public class SecurityUtil {

    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userPk = userDetails.getMember().getId();
        return userPk;
    }


    //로그인 유저의 이메일 정보를 얻어오는 메소드
    public static String getCurrentMemberEmail(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null){
            throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        return authentication.getName();
    }

}