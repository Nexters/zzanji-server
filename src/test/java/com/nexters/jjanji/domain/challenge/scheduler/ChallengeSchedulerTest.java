package com.nexters.jjanji.domain.challenge.scheduler;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.scheduler.ChallengeScheduler;
import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChallengeSchedulerTest {

    @Autowired ChallengeScheduler challengeScheduler;
    @Autowired ChallengeRepository challengeRepository;
    @Autowired ParticipationRepository participationRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PlanRepository planRepository;

    @Test
    @DisplayName("챌린지 생성 스케줄러 테스트")
    void weeklyChallengeTask() {
        //given
        Member member = memberRepository.save(
                Member.builder()
                        .deviceId("test-device-id")
                        .build()
        );

        LocalDateTime testDate = LocalDateTime.of(2021, 8, 1, 0, 0, 0);

        // Prev Challenge: closed로 상태변경
        Challenge prevChallenge = challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate.plusDays(-7))
                        .endAt(testDate.plusDays(0))
                        .build());
        prevChallenge.openChallenge();

        // Current Challenge: opened로 상태변경 및 Next Challenge로 복사
        Challenge currentChallenge = challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate.plusDays(0))
                        .endAt(testDate.plusDays(7))
                        .build());
        Participation currentParticipation = participationRepository.save(
                Participation.builder()
                        .challenge(currentChallenge)
                        .goalAmount(10000L)
                        .member(memberRepository.getReferenceById(member.getId()))
                        .build()
        );
        planRepository.saveAll(List.of(
                Plan.builder()
                        .participation(currentParticipation)
                        .categoryGoalAmount(1000L)
                        .category(PlanCategory.FOOD)
                        .build(),
                Plan.builder()
                        .participation(currentParticipation)
                        .categoryGoalAmount(2000L)
                        .category(PlanCategory.BEAUTY)
                        .build(),
                Plan.builder()
                        .participation(currentParticipation)
                        .categoryGoalAmount(3000L)
                        .category(PlanCategory.EATOUT)
                        .build()
        ));

        //when
        challengeScheduler.weeklyChallengeTask();

        //then
        Challenge nextChallenge = challengeRepository.findChallengeByState(ChallengeState.PRE_OPENED).get();
        Participation nextParticipation = participationRepository.findByMemberAndChallenge(member, nextChallenge).get();

        // 이전 참가내역 및 계획 복사 확인
        List<Plan> all = planRepository.findAll();
        assertThat(all).hasSize(6);

        List<Plan> planList = planRepository.findByParticipationId(nextParticipation.getId());
        assertThat(planList).hasSize(3);
        assertThat(planList.get(0).getParticipation()).isEqualTo(nextParticipation);

        // 상태값 확인
        assertThat(prevChallenge.getState()).isEqualTo(ChallengeState.CLOSED);
        assertThat(currentChallenge.getState()).isEqualTo(ChallengeState.OPENED);
        assertThat(nextChallenge.getState()).isEqualTo(ChallengeState.PRE_OPENED);
    }

}
