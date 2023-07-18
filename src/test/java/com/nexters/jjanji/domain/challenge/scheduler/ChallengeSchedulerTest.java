package com.nexters.jjanji.domain.challenge.scheduler;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.scheduler.ChallengeScheduler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class ChallengeSchedulerTest {

    @Autowired
    ChallengeScheduler challengeScheduler;
    @Autowired ChallengeRepository challengeRepository;

    @Test
    @DisplayName("챌린지 생성 스케줄러 테스트")
    @Disabled
    void createChallengeTask() {
        //when
        challengeScheduler.createChallengeTask();

        //then
        Challenge challenge = challengeRepository.findAll().get(0);

        Assertions.assertThat(challengeRepository.count()).isOne();
        Assertions.assertThat(challenge.getStartAt().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth() + 7);
        Assertions.assertThat(challenge.getEndAt().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth() + 14);
    }

}
