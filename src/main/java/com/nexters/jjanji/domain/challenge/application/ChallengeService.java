package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationDao;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.CreateCategoryPlanRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.UpdateGoalAmountRequestDto;
import com.nexters.jjanji.domain.challenge.dto.response.ParticipationResponseDto;
import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import com.nexters.jjanji.global.exception.AlreadyParticipateException;
import com.nexters.jjanji.global.exception.NotParticipateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final ParticipationDao participationDao;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    @Transactional
    public Challenge createChallengeAndUpdateState() {
        updatePreviousChallenges();
        LocalDateTime todayDate = getTodayDate();
        Challenge challenge = Challenge.builder()
                .startAt(todayDate.plusDays(7))
                .endAt(todayDate.plusDays(14))
                .build();
        return challengeRepository.save(challenge);
    }

    private void updatePreviousChallenges() {
        //TODO: 해당 매서드 정리 필요.
        //TODO: currentChallenge 와 nextChallenge 의 캐싱이 필요해 보임. -> 변경이 예측 가능한 데이터
        //TODO: 초기 런칭 단계에만 필요한 예외 및 옵셔널은 어떻게 관리해야 하는지에 대한 고민 필요. 실제 프로덕션에서는 무의미.
        Optional<Challenge> optionalCurrentChallenge = challengeRepository.findChallengeByState(ChallengeState.PRE_OPENED);
        Optional<Challenge> optionalNextChallenge = challengeRepository.findChallengeByState(ChallengeState.OPENED);
        if (optionalCurrentChallenge.isPresent()) {
            Challenge currentChallenge = optionalCurrentChallenge.get();
            currentChallenge.closeChallenge();
        }
        if (optionalNextChallenge.isPresent()) {
            Challenge nextChallenge = optionalNextChallenge.get();
            nextChallenge.openChallenge();
        }
    }

    private LocalDateTime getTodayDate() {
        return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    @Transactional
    public void participateNextChallenge(Long memberId, ParticipateRequestDto participateRequestDto) {
        Member member = memberRepository.getReferenceById(memberId);
        Challenge nextChallenge = challengeRepository.findNextChallenge();
        if (participationRepository.existsByMemberAndChallenge(member, nextChallenge)) {
            throw new AlreadyParticipateException();
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
                .orElseThrow(NotParticipateException::new);

        List<Plan> planList = createCategoryPlanRequestDtoList.stream()
                .map(createCategoryPlanRequestDto -> Plan.builder()
                        .participation(participation)
                        .category(createCategoryPlanRequestDto.getCategory())
                        .categoryGoalAmount(createCategoryPlanRequestDto.getGoalAmount())
                        .build())
                .toList();

        planRepository.saveAll(planList);
    }

    @Transactional(readOnly = true)
    public List<ParticipationResponseDto> getParticipateList(Long memberId, Long cursor, Long size) {
        List<ParticipationResponseDto> participateList = participationDao.getParticipateList(memberId, cursor, size);
        participateList.forEach(participationResponseDto -> {
            List<Plan> planList = planRepository.findByParticipationId(participationResponseDto.getParticipationId());
            participationResponseDto.setPlanList(planList);
        });
        return participateList;
    }

    @Transactional
    public void updateTotalGoalAmount(Long memberId, UpdateGoalAmountRequestDto updateGoalAmountRequestDto) {
        Member member = memberRepository.getReferenceById(memberId);
        Challenge nextChallenge = challengeRepository.findNextChallenge();
        Participation participation = participationRepository.findByMemberAndChallenge(member, nextChallenge)
                .orElseThrow(NotParticipateException::new);
        participation.updateGoalAmount(updateGoalAmountRequestDto.getGoalAmount());
    }
}
