package com.nexters.jjanji.challenge.application;

import com.nexters.jjanji.challenge.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.application.ChallengeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @InjectMocks
    ChallengeService challengeService;
    @Mock ChallengeRepository challengeRepository;

    @Test
    @DisplayName("챌린지 생성 성공")
    void createChallenge() {
        // given

        // when
        challengeService.createChallenge();

        // then
        BDDMockito.then(challengeRepository).should(times(1)).save(any());
    }
}
