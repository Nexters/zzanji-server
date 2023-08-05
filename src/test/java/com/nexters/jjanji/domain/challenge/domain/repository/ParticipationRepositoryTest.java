package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ParticipationRepositoryTest {

    @Autowired ChallengeRepository challengeRepository;
    @Autowired ParticipationRepository participationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private SpendingHistoryRepository spendingHistoryRepository;

    @Test
    @DisplayName("다음 챌린지에 이전 참가자들의 참가 내역이 복사된다.")
    void copyPreviousParticipate() {
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
        participationRepository.save(
                Participation.builder()
                        .challenge(currentChallenge)
                        .goalAmount(10000L)
                        .member(memberRepository.getReferenceById(member.getId()))
                        .build()
        );
        Challenge nextChallenge = challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate.plusDays(7))
                        .endAt(testDate.plusDays(14))
                        .build());

        // when
        participationRepository.copyPreviousParticipate(currentChallenge.getId(), nextChallenge.getId());

        // then
        Optional<Participation> byMemberAndChallenge = participationRepository.findByMemberAndChallenge(member, nextChallenge);
        assertThat(byMemberAndChallenge).isPresent();
        assertThat(byMemberAndChallenge.get().getGoalAmount()).isEqualTo(10000L);
        assertThat(byMemberAndChallenge.get().getMember()).isEqualTo(member);
        assertThat(byMemberAndChallenge.get().getChallenge()).isEqualTo(nextChallenge);
    }

    @Test
    @DisplayName("참가 내역이 모두 삭제된다.")
    void deleteParticipateHistory() {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .deviceId("test-device-id")
                        .build()
        );
        LocalDateTime testDate = LocalDateTime.of(2021, 8, 1, 0, 0, 0);
        Challenge challenge = challengeRepository.save(
                Challenge.builder()
                        .startAt(testDate.plusDays(0))
                        .endAt(testDate.plusDays(7))
                        .build());

        Participation participation = participationRepository.save(
                Participation.builder()
                        .challenge(challenge)
                        .goalAmount(10000L)
                        .member(memberRepository.getReferenceById(member.getId()))
                        .build()
        );
        Plan plan = planRepository.save(Plan.builder()
                .participation(participation)
                .category(PlanCategory.FOOD)
                .categoryGoalAmount(10000L)
                .build());

        spendingHistoryRepository.save(SpendingHistory.builder()
                .plan(plan)
                .title("title")
                .memo("memo")
                .spendAmount(0L)
                .build());

        // when
        participationRepository.deleteParticipationBulk(participation.getId());

        // then
        assertThat(participationRepository.count()).isZero();
        assertThat(planRepository.count()).isZero();
        assertThat(spendingHistoryRepository.count()).isZero();
    }
}
