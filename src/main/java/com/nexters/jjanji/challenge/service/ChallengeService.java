package com.nexters.jjanji.challenge.service;

import com.nexters.jjanji.challenge.domain.Challenge;
import com.nexters.jjanji.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public void createChallenge() {
        LocalDateTime todayDate = getTodayDate();
        Challenge challenge = Challenge.builder()
                .startAt(todayDate.plusDays(7))
                .endAt(todayDate.plusDays(14))
                .build();
        challengeRepository.save(challenge);
    }

    private LocalDateTime getTodayDate() {
        return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
}
