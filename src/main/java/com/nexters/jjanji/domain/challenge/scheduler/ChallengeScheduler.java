package com.nexters.jjanji.domain.challenge.scheduler;

import com.nexters.jjanji.domain.challenge.application.ChallengeService;
import com.nexters.jjanji.domain.challenge.domain.Challenge;
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
    public void weeklyChallengeTask() {
        Challenge challenge = challengeService.weeklySchedulerProcess();
        log.info("createChallengeTask|{}month {}week challenge task is completed",
                challenge.getMonth(), challenge.getWeek());
    }
}
