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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final ParticipationDao participationDao;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final TransactionTemplate transactionTemplate;

    public Challenge weeklySchedulerProcess() {
        Challenge prevChallenge = challengeRepository.findChallengeByState(ChallengeState.OPENED)
                .orElseThrow(() -> new RuntimeException("weeklySchedulerProcess| 다음 챌린지가 존재하지 않습니다."));
        Challenge currentChallenge = challengeRepository.findChallengeByState(ChallengeState.PRE_OPENED)
                .orElseThrow(() -> new RuntimeException("weeklySchedulerProcess| 진행중인 챌린지가 존재하지 않습니다."));

        Challenge nextChallenge = transactionTemplate.execute(status -> {
            updateChallengesState(prevChallenge, currentChallenge);
            return createAndGetNextChallenge();
        });

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status){
                participationRepository.copyPreviousParticipate(currentChallenge.getId(), nextChallenge.getId());
                planRepository.copyPreviousPlans(currentChallenge.getId(), nextChallenge.getId());
            }
        });

        return nextChallenge;
    }

    private Challenge createAndGetNextChallenge() {
        LocalDateTime todayDate = getTodayDate();
        Challenge challenge = Challenge.builder()
                .startAt(todayDate.plusDays(7))
                .endAt(todayDate.plusDays(14))
                .build();
        return challengeRepository.save(challenge);
    }

    private void updateChallengesState(Challenge prevChallenge, Challenge currentChallenge) {
        if (prevChallenge.getState() != ChallengeState.OPENED) {
            throw new RuntimeException("진행중이지 않은 챌린지는 종료될 수 없습니다.");
        }
        prevChallenge.closeChallenge();
        if (currentChallenge.getState() != ChallengeState.PRE_OPENED) {
            throw new RuntimeException("진행중이지 않은 챌린지만 시작될 수 있습니다.");
        }
        currentChallenge.openChallenge();
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
