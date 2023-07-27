package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.dto.request.CreateCategoryPlanRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.challenge.dto.request.UpdateGoalAmountRequestDto;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import com.nexters.jjanji.global.exception.AlreadyParticipateException;
import com.nexters.jjanji.global.exception.NotParticipateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @InjectMocks
    ChallengeService challengeService;
    @Mock ChallengeRepository challengeRepository;
    @Mock ParticipationRepository participationRepository;
    @Mock MemberRepository memberRepository;
    @Mock PlanRepository planRepository;

    @Test
    @DisplayName("다음주 챌린지 참가 요청 - 성공")
    void participateNextChallenge() {
        // given
        ParticipateRequestDto request = ParticipateRequestDto.builder()
                .goalAmount(1000L)
                .build();

        given(participationRepository.existsByMemberAndChallenge(any(), any())).willReturn(false);

        // when
        challengeService.participateNextChallenge(1L, request);

        // then
        then(participationRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("다음주 챌린지 참가 요청 - 이미 챌린지에 참가중인 경우 예외가 발생한다")
    void participateNextChallenge_already_participated() {
        // given
        ParticipateRequestDto request = ParticipateRequestDto.builder()
                .goalAmount(1000L)
                .build();

        given(participationRepository.existsByMemberAndChallenge(any(), any())).willReturn(true);

        // when, then

        Assertions.assertThatThrownBy(() -> {
            challengeService.participateNextChallenge(1L, request);
        }).isInstanceOf(AlreadyParticipateException.class);
        then(participationRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("카테고리별 목표 설정 - 성공")
    void addCategoryPlan() {
        // given
        List<CreateCategoryPlanRequestDto> request = List.of(
                CreateCategoryPlanRequestDto.builder()
                        .category(PlanCategory.TRANSPORTATION)
                        .goalAmount(10000L)
                        .build(),
                CreateCategoryPlanRequestDto.builder()
                        .category(PlanCategory.FOOD)
                        .goalAmount(200000L)
                        .build()
        );

        given(participationRepository.findByMemberAndChallenge(any(), any())).willReturn(Optional.of(new Participation()));

        // when
        challengeService.addCategoryPlan(1L, request);

        // then
        then(planRepository).should(times(1)).saveAll(any());
    }

    @Test
    @DisplayName("카테고리별 목표 설정 - 아직 챌린지에 참여하지 않았으면, 예외를 반환한다.")
    void addCategoryPlan_not_participated_challenge() {
        // given
        List<CreateCategoryPlanRequestDto> request = List.of(
                CreateCategoryPlanRequestDto.builder()
                        .category(PlanCategory.TRANSPORTATION)
                        .goalAmount(10000L)
                        .build(),
                CreateCategoryPlanRequestDto.builder()
                        .category(PlanCategory.FOOD)
                        .goalAmount(200000L)
                        .build()
        );

        given(participationRepository.findByMemberAndChallenge(any(), any())).willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> {
            challengeService.addCategoryPlan(1L, request);
        }).isInstanceOf(NotParticipateException.class);
    }

    @Test
    @DisplayName("전체 목표 금액 수정 - 성공")
    void updateTotalGoalAmount() {
        // given
        Participation mockParticipation = new Participation();
        UpdateGoalAmountRequestDto updateGoalAmountRequestDto = UpdateGoalAmountRequestDto.builder()
                .goalAmount(10000L)
                .build();
        given(participationRepository.findByMemberAndChallenge(any(), any())).willReturn(Optional.of(mockParticipation));

        // when, then
        challengeService.updateTotalGoalAmount(1L, updateGoalAmountRequestDto);
    }

}
