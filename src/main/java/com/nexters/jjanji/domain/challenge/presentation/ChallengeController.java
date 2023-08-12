package com.nexters.jjanji.domain.challenge.presentation;

import com.nexters.jjanji.domain.challenge.application.ChallengeService;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.dto.request.CreateCategoryPlanRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.UpdateGoalAmountRequestDto;
import com.nexters.jjanji.domain.challenge.dto.response.ParticipationResponseDto;
import com.nexters.jjanji.domain.member.application.MemberService;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import com.nexters.jjanji.domain.notification.domain.repository.NotificationInfoRepository;
import com.nexters.jjanji.global.auth.MemberContext;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final NotificationInfoRepository notificationInfoRepository;

    @PostMapping("/participate/test")
    public void testParticipate() {
        Long memberId = MemberContext.getMember();
        String deviceId = MemberContext.getDevice();
        memberRepository.deleteById(memberId);
        notificationInfoRepository.deleteById(deviceId);

        final Member member = memberService.createMember(deviceId);
        challengeService.testParticipate(member.getId());
    }

    @GetMapping("/participate")
    public List<ParticipationResponseDto> getParticipateList(@RequestParam(required = false) Long cursor, @RequestParam Long size) {
        Long memberId = MemberContext.getMember();
        return challengeService.getParticipateList(memberId, cursor, size);
    }

    @DeleteMapping("/participate/{participationId}")
    public void deleteParticipate(@PathVariable Long participationId) {
        Long memberId = MemberContext.getMember();
        challengeService.deleteParticipate(memberId, participationId);
    }

    @PutMapping("/participate/goalAmount")
    public void updateGoalAmount(@RequestBody UpdateGoalAmountRequestDto updateGoalAmountRequestDto) {
        Long memberId = MemberContext.getMember();
        challengeService.updateTotalGoalAmount(memberId, updateGoalAmountRequestDto);
    }

    @PostMapping("/participate")
    public void participateNextChallenge(@Valid @RequestBody ParticipateRequestDto participateRequestDto) {
        Long memberId = MemberContext.getMember();
        challengeService.participateNextChallenge(memberId, participateRequestDto);
    }

    @PostMapping("/plan/category")
    public void addCategoryPlan(@RequestBody List<CreateCategoryPlanRequestDto> createCategoryPlanRequestDtoList) {
        Long memberId = MemberContext.getMember();
        challengeService.addCategoryPlan(memberId, createCategoryPlanRequestDtoList);
    }
}
