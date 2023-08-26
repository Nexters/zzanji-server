package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChallengeRepositoryTest {
    @Autowired ChallengeRepository challengeRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ParticipationRepository participationRepository;
    @Autowired PlanRepository planRepository;

    @Test
    @DisplayName("다음 챌린지 조회(마지막 챌린지) 쿼리")
    void findNextChallenge() {
        // given
        LocalDateTime testDate = LocalDateTime.of(2021, 8, 1, 0, 0, 0);

        challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate)
                        .endAt(testDate.plusDays(7))
                        .build()
        );
        challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate.plusDays(7))
                        .endAt(testDate.plusDays(14))
                        .build()
        );
        Challenge lastCreatedChallenge = challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate.plusDays(14))
                        .endAt(testDate.plusDays(21))
                        .build()
        );
        // when
        Challenge nextChallenge = challengeRepository.findNextChallenge();

        // then
        assertThat(nextChallenge.getId()).isEqualTo(lastCreatedChallenge.getId());
    }

    @Test
    @DisplayName("해당 Plan의 Challenge 조회")
    @Transactional
    void findDistinctChallengeByPlan(){
        //given
        final LocalDateTime testDay = LocalDateTime.of(2023,7,13,0,0,0);
        Challenge challenge1 = challengeRepository.save(
                Challenge.builder()
                        .startAt(testDay)
                        .endAt(testDay.plusDays(7))
                        .build());

        Member member1 = memberRepository.save(
                Member.builder()
                        .deviceId("member1")
                        .build()
        );

        Participation participation1 = participationRepository.save(
                Participation.builder()
                        .member(member1)
                        .challenge(challenge1)
                        .goalAmount(1000L)
                        .build()
        );

        Plan plan1 = planRepository.save(
                Plan.builder()
                        .participation(participation1)
                        .category(PlanCategory.FOOD)
                        .categoryGoalAmount(100L)
                        .build()
        );
        Plan plan2 = planRepository.save(
                Plan.builder()
                        .participation(participation1)
                        .category(PlanCategory.FOOD)
                        .categoryGoalAmount(200L)
                        .build()
        );
        Plan plan3 = planRepository.save(
                Plan.builder()
                        .participation(participation1)
                        .category(PlanCategory.FOOD)
                        .categoryGoalAmount(300L)
                        .build()
        );

        //when
        Optional<Challenge> findChallenge = challengeRepository.findChallengeByPlanId(plan3.getId());

        //then
        assertThat(findChallenge).isPresent();
        assertThat(findChallenge.get().getId()).isEqualTo(challenge1.getId());
        assertThat(findChallenge.get().getStartAt()).isEqualTo(challenge1.getStartAt());
        assertThat(findChallenge.get().getEndAt()).isEqualTo(challenge1.getEndAt());
    }
}