package com.nexters.jjanji.domain.member.application;

import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(String deviceId) {
        Member member = Member.builder()
                .deviceId(deviceId)
                .build();
        return memberRepository.save(member);
    }
}
