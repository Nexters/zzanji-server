package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.dto.response.ParticipationResponseDto;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.domain.member.domain.Member;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ParticipationDaoTest {

    @Autowired ChallengeRepository challengeRepository;
    @Autowired ParticipationRepository participationRepository;
    @Autowired ParticipationDao participationDao;
    @Autowired MemberRepository memberRepository;
    @Autowired PlanRepository planRepository;

    Member member;

    @BeforeEach
    void setUp() {
        //given
        member = memberRepository.save(
                Member.builder()
                        .deviceId("test")
                        .build()
        );

        for (int i = 0; i < 5; i++) {
            //7일 주기로 챌린지 5개 생성
            Challenge challenge = challengeRepository.save(
                    Challenge.builder()
                            .startAt(LocalDateTime.now().plusDays(i * 7))
                            .endAt(LocalDateTime.now().plusDays((i * 7) + 7))
                            .build()
            );

            Participation participation = participationRepository.save(
                    Participation.builder()
                            .challenge(challenge)
                            .goalAmount(10000L)
                            .member(member)
                            .build()
            );

            //create two plans
            Plan plan1 = planRepository.save(
                    Plan.builder()
                            .category(PlanCategory.TRANSPORTATION)
                            .categoryGoalAmount(10000L * i)
                            .participation(participation)
                            .build()
            );
            plan1.plusCategorySpendAmount(10000L * i);
            Plan plan2 = planRepository.save(
                    Plan.builder()
                            .category(PlanCategory.TRANSPORTATION)
                            .categoryGoalAmount(10000L * i)
                            .participation(participation)
                            .build()
            );
            plan2.plusCategorySpendAmount(10000L * i);
            planRepository.saveAll(List.of(plan1, plan2));
        }
    }

    @Test
    @DisplayName("참여 내역 전체 조회")
    void getParticipateList() {
        //when
        List<ParticipationResponseDto> participateList = participationDao.getParticipateList(member.getId(), null, 10L);
        Assertions.assertThat(participateList).hasSize(5);
    }

    @Test
    @DisplayName("페이징 테스트")
    void getParticipateList_paging() {
        //when
        List<ParticipationResponseDto> participateList = participationDao.getParticipateList(member.getId(), null, 2L);
        Assertions.assertThat(participateList).hasSize(2);

        participateList = participationDao.getParticipateList(member.getId(), participateList.get(1).getParticipationId(), 2L);
        Assertions.assertThat(participateList).hasSize(2);

        participateList = participationDao.getParticipateList(member.getId(), participateList.get(1).getParticipationId(), 2L);
        Assertions.assertThat(participateList).hasSize(1);
    }
}