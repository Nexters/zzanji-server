package com.nexters.jjanji;

import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.ParticipationRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class DummyConfig {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final PlanRepository planRepository;
    private final SpendingHistoryRepository spendingHistoryRepository;
    //@Bean
    //@Profile("dev")
    public DummyDataInit dummyDataInit(){
        return new DummyDataInit(memberRepository, challengeRepository, participationRepository, planRepository, spendingHistoryRepository);
    }
}
