package com.didacto.controller.v1.member;

import com.didacto.domain.Member;
import com.didacto.dto.member.MemberResponse;
import com.didacto.service.member.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/member/query")
public class MemberQueryController {
    private final MemberQueryService memberQueryService;

    @GetMapping("{memberId}")
    public MemberResponse query(@PathVariable("memberId") Long memberId) {
        Member member = memberQueryService.query(memberId);

        return new MemberResponse(member);
    }
}
