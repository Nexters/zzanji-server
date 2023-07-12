package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.CreateCategoryPlanRequestDto;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    public void createChallenge() {
        LocalDateTime todayDate = getTodayDate();
        Challenge challenge = Challenge.builder()
                .startAt(todayDate.plusDays(7))
                .endAt(todayDate.plusDays(14))
                .build();
        challengeRepository.save(challenge);
    }

    private LocalDateTime getTodayDate() {
        return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    @Transactional
    public void participateNextChallenge(Long memberId, ParticipateRequestDto participateRequestDto) {
        Member member = memberRepository.getReferenceById(memberId);
        Challenge nextChallenge = challengeRepository.findNextChallenge();
        if (participationRepository.existsByMemberAndChallenge(member, nextChallenge)) {
            throw new IllegalStateException("이미 다음 챌린지에 참여중입니다.");
        }
        Participation participation = Participation.builder()
                .member(member)
                .challenge(nextChallenge)
                .goalAmount(participateRequestDto.getGoalAmount())
                .build();
        participationRepository.save(participation);
    }

    @Transactional
    public void addCategoryPlan(Long memberId, List<CreateCategoryPlanRequestDto> createCategoryPlanRequestDtoList) {
        Member member = memberRepository.getReferenceById(memberId);
        Challenge nextChallenge = challengeRepository.findNextChallenge();
        Participation participation = participationRepository.findByMemberAndChallenge(member, nextChallenge)
                .orElseThrow(() -> new IllegalStateException("아직 챌린지에 참여하지 않았습니다."));

        List<Plan> planList = createCategoryPlanRequestDtoList.stream()
                .map(createCategoryPlanRequestDto -> Plan.builder()
                        .participation(participation)
                        .category(createCategoryPlanRequestDto.getCategory())
                        .categoryGoalAmount(createCategoryPlanRequestDto.getGoalAmount())
                        .build())
                .toList();

        planRepository.saveAll(planList);
    }
}
