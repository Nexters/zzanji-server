package com.nexters.jjanji;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
public class DummyDataInit {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final PlanRepository planRepository;
    private final SpendingHistoryRepository spendingHistoryRepository;

    /**
     * <더미 데이터 요약>
     * [Challenge]
     * - challengeId:1 -> 2023.7.17~2023.7.24 Closed
     * - challengeId:2 -> 2023.7.24~2023.7.31 Open
     * - challengeId:3 -> 2023.7.31~2023.8.7 Pre-Open
     *
     * [User]
     * - memberId:1 -> deviceId:"dummyUser"
     *
     * [Participant]
     * - participantId:1 -> memberId:1, challengeId:1
     * - participantId:2 -> memberId:1, challengeId:2
     *
     * [Plan]
     * - planId:1 -> participantId:2, food(식비)
     * - planId:2 -> participantId:2, eatOut(외식비)
     * - planId:3 -> participantId:2, coffee(커피)
     * - planId:4 -> participantId:2, transportation(교통/유류비)
     * - planId:5 -> participantId:2, beauty(의류/미용비)
     * - planId:6 -> participantId:2, culture(문화비)
     * - planId:7 -> participantId:2, nestEgg(비상금)
     *
     * [Spending]
     * - spendingId:1 -> planId:1, 5000원 소비
     * - spendingId:2 -> planId:2, 5000원 소비
     * </더미>
     */
    @EventListener(ApplicationReadyEvent.class)
    public void dummyDate(){
        Challenge dummyChallenge1 = Challenge.builder()
                .startAt(LocalDateTime.of(2023,7,17,0,0,0))
                .endAt(LocalDateTime.of(2023,7,24,0,0,0))
                .build();
        dummyChallenge1.closeChallenge();
        challengeRepository.save(dummyChallenge1);
        Challenge dummyChallenge2 = Challenge.builder()
                .startAt(LocalDateTime.of(2023,7,24,0,0,0))
                .endAt(LocalDateTime.of(2023,7,31,0,0,0))
                .build();
        dummyChallenge2.openChallenge();
        challengeRepository.save(dummyChallenge2);
        Challenge dummyChallenge3 = Challenge.builder()
                .startAt(LocalDateTime.of(2023,7,31,0,0,0))
                .endAt(LocalDateTime.of(2023,8,7,0,0,0))
                .build();
        challengeRepository.save(dummyChallenge3);

        Member dummyUser = Member.builder()
                .deviceId("dummyUser")
                .build();
        memberRepository.save(dummyUser);

        Participation dummyParticipation1 = Participation.builder()
                .member(dummyUser)
                .challenge(dummyChallenge1)
                .goalAmount(100000L)
                .build();
        participationRepository.save(dummyParticipation1);
        Participation dummyParticipation2 = Participation.builder()
                .member(dummyUser)
                .challenge(dummyChallenge2)
                .goalAmount(100000L)
                .build();
        participationRepository.save(dummyParticipation2);

        Plan coffee = Plan.builder()
                .participation(dummyParticipation2)
                .category(PlanCategory.COFFEE)
                .categoryGoalAmount(20000L)
                .build();
        planRepository.save(coffee);
        Plan food = Plan.builder()
                .participation(dummyParticipation2)
                .category(PlanCategory.FOOD)
                .categoryGoalAmount(20000L)
                .build();
        planRepository.save(food);
        Plan eatOut = Plan.builder()
                .participation(dummyParticipation2)
                .category(PlanCategory.EATOUT)
                .categoryGoalAmount(10000L)
                .build();
        planRepository.save(eatOut);
        Plan transportation = Plan.builder()
                .participation(dummyParticipation2)
                .category(PlanCategory.TRANSPORTATION)
                .categoryGoalAmount(20000L)
                .build();
        planRepository.save(transportation);
        Plan beauty = Plan.builder()
                .participation(dummyParticipation2)
                .category(PlanCategory.BEAUTY)
                .categoryGoalAmount(10000L)
                .build();
        planRepository.save(beauty);
        Plan culture = Plan.builder()
                .participation(dummyParticipation2)
                .category(PlanCategory.CULTURE)
                .categoryGoalAmount(15000L)
                .build();
        planRepository.save(culture);
        Plan nestEgg = Plan.builder()
                .participation(dummyParticipation2)
                .category(PlanCategory.NESTEGG)
                .categoryGoalAmount(5000L)
                .build();
        planRepository.save(nestEgg);

        SpendingHistory dummySpending1 = SpendingHistory.builder()
                .plan(coffee)
                .title("스타벅스")
                .memo("짠지 팀원과 즐거운 커피 시간")
                .spendAmount(5000L)
                .build();
        spendingHistoryRepository.save(dummySpending1);
        coffee.plusCategorySpendAmount(5000L);
        SpendingHistory dummySpending2 = SpendingHistory.builder()
                .plan(food)
                .title("롯데리아")
                .memo("짠지 팀원과 즐거운 햄버거 시간")
                .spendAmount(5000L)
                .build();
        spendingHistoryRepository.save(dummySpending2);
        food.plusCategorySpendAmount(5000L);
    }
}
