package com.nexters.jjanji.domain.challenge.scheduler;

import com.nexters.jjanji.domain.challenge.application.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChallengeScheduler {

    private final ChallengeService challengeService;

    @Scheduled(cron = "0 0 0 ? * MON", zone = "Asia/Seoul")
    public void createChallengeTask() {
        challengeService.createChallengeAndUpdateState();
        log.info("createChallengeTask|create challenge task is completed");
    }
}
