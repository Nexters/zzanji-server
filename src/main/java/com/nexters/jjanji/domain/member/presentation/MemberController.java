package com.nexters.jjanji.domain.member.presentation;

import com.nexters.jjanji.global.auth.MemberContext;
import com.nexters.jjanji.domain.member.dto.response.TestResponseDto;
import com.nexters.jjanji.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test")
    public TestResponseDto test() {
        return new TestResponseDto(MemberContext.getContext());
    }
}
