package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ChallengeDaoTest {

    @Autowired
    ChallengeDao challengeDao;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChallengeRepository challengeRepository;

    @Test
    @DisplayName("다음(마지막 생성된) 챌린지 조회")
    void something() {
        // given
//        Challenge.builder()
//                .endAt()
//                .startAt()
//                .
//                .build();
//        challengeRepository.saveAll();
        // when
        // then
    }
}