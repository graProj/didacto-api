package com.didacto.api.v1.member;

import com.didacto.common.response.CommonResponse;
import com.didacto.config.security.SecurityUtil;
import com.didacto.domain.Member;
import com.didacto.dto.member.MemberEditRequest;
import com.didacto.repository.member.MemberRepository;
import com.didacto.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Tag(name = "MEMBER API", description = "회원과 관련된 API") // Swagger Docs : API 이름
@RequestMapping("api/v1/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;




    @Operation(summary = "AUTH_04 : 회원 정보 수정 API", description = "회원 정보를 수정한다.")
    @PutMapping("/members")
    public CommonResponse editMemberInfo(@RequestBody MemberEditRequest memberEditRequest){


        String userEmail = SecurityUtil.getCurrentMemberEmail();
        memberService.editMemberInfo(userEmail, memberEditRequest);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail,
                memberEditRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new CommonResponse<>(true, HttpStatus.OK, "회원 정보 수정에 성공했습니다.", null);
    }


    @DeleteMapping("/members")
    public CommonResponse deleteMemberInfo() {
        String userEmail = SecurityUtil.getCurrentMemberEmail();
        memberService.deleteMember(userEmail);
        return new CommonResponse<>(true, HttpStatus.OK, "회원이 탈퇴되었습니다.", null);
    }
}
