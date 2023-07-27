package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
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

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PlanRepositoryTest {

    @Autowired ChallengeRepository challengeRepository;
    @Autowired ParticipationRepository participationRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PlanRepository planRepository;

    @Test
    @DisplayName("다음 챌린지에 이전 참가자들의 참가 내역이 복사된다.")
    @Disabled
    void copyPreviousPlans() {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .deviceId("test-device-id")
                        .build()
        );
        LocalDateTime testDate = LocalDateTime.of(2021, 8, 1, 0, 0, 0);
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
        Challenge nextChallenge = challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate.plusDays(7))
                        .endAt(testDate.plusDays(14))
                        .build());
        Participation nextParticipation = participationRepository.save(
                Participation.builder()
                        .challenge(nextChallenge)
                        .goalAmount(10000L)
                        .member(memberRepository.getReferenceById(member.getId()))
                        .build()
        );

        // when
        planRepository.copyPreviousPlans(currentChallenge.getId(), nextChallenge.getId());

        // then
        List<Plan> all = planRepository.findAll();
        assertThat(all).hasSize(6);

        List<Plan> planList = planRepository.findByParticipationId(nextParticipation.getId());
        assertThat(planList).hasSize(3);
        assertThat(planList.get(0).getParticipation()).isEqualTo(nextParticipation);

    }
}