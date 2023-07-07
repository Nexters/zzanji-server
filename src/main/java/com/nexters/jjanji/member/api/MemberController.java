package com.nexters.jjanji.member.api;

import com.nexters.jjanji.common.CommonResponse;
import com.nexters.jjanji.common.auth.MemberContext;
import com.nexters.jjanji.member.service.MemberService;
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
    public CommonResponse<Long> test() {
        Long userId = MemberContext.getContext();
        return new CommonResponse<>(userId);
    }
}
