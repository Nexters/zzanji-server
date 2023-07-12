package com.nexters.jjanji.domain.challenge.presentation;

import com.nexters.jjanji.domain.challenge.application.ChallengeService;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.global.auth.MemberContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/participate")
    public void participateNextChallenge(@Valid @RequestBody ParticipateRequestDto participateRequestDto) {
        Long memberId = MemberContext.getContext();
        System.out.println("participateRequestDto = " + participateRequestDto.getGoalAmount());
        challengeService.participateNextChallenge(memberId, participateRequestDto);
    }
}
