package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.application.ChallengeService;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.dto.request.ParticipateRequestDto;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    @DisplayName("챌린지 생성 성공")
    void createChallenge() {
        // given

        // when
        challengeService.createChallenge();

        // then
        then(challengeRepository).should(times(1)).save(any());
    }

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
        }).isInstanceOf(IllegalStateException.class);
        then(participationRepository).should(never()).save(any());
    }

}
