package com.nexters.jjanji.domain.challenge.presentation;

import com.nexters.jjanji.domain.challenge.application.ChallengeService;
import com.nexters.jjanji.domain.challenge.dto.request.CreateCategoryPlanRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.challenge.dto.response.ParticipationResponseDto;
import com.nexters.jjanji.global.auth.MemberContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/participate")
    public List<ParticipationResponseDto> getParticipateList(@RequestParam Long cursor, @RequestParam Long limit) {
        Long memberId = MemberContext.getContext();
        return challengeService.getParticipateList(memberId, cursor, limit);
    }

    @PostMapping("/participate")
    public void participateNextChallenge(@Valid @RequestBody ParticipateRequestDto participateRequestDto) {
        Long memberId = MemberContext.getContext();
        challengeService.participateNextChallenge(memberId, participateRequestDto);
    }

    @PostMapping("/plan/category")
    public void addCategoryPlan(@RequestBody List<CreateCategoryPlanRequestDto> createCategoryPlanRequestDtoList) {
        Long memberId = MemberContext.getContext();
        challengeService.addCategoryPlan(memberId, createCategoryPlanRequestDtoList);
    }
}
