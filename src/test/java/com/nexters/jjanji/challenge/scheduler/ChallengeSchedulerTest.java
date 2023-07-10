package com.nexters.jjanji.challenge.scheduler;

import com.nexters.jjanji.challenge.domain.Challenge;
import com.nexters.jjanji.challenge.repository.ChallengeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
class ChallengeSchedulerTest {

    @Autowired ChallengeScheduler challengeScheduler;
    @Autowired ChallengeRepository challengeRepository;

    @Test
    @DisplayName("챌린지 생성 스케줄러 테스트")
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
