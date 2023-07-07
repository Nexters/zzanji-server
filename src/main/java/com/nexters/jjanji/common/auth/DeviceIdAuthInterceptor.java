package com.nexters.jjanji.common.auth;

import com.nexters.jjanji.member.domain.Member;
import com.nexters.jjanji.member.domain.MemberRepository;
import com.nexters.jjanji.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DeviceIdAuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String deviceId = request.getHeader("Authorization");
        if (deviceId == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다.");
            return false;
        }
        Optional<Member> optionalMember = memberRepository.findByDeviceId(deviceId);
        Member member = optionalMember.orElse(null);
        if (optionalMember.isEmpty()) {
            member = memberService.createMember(deviceId);
        }
        MemberContext.CONTEXT.set(member.getId());
        log.info("[유저 로그인 정보] id:{}, deviceId:{}", member.getId(), member.getDeviceId());
        return true;
    }
}
