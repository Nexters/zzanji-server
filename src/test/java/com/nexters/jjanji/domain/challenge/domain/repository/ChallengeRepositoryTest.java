package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChallengeRepositoryTest {
    @Autowired ChallengeRepository challengeRepository;

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
        Assertions.assertThat(nextChallenge.getId()).isEqualTo(lastCreatedChallenge.getId());
    }
}