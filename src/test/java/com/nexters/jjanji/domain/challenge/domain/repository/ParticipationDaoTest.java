package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.challenge.dto.PrevAndNextPostVO;
import com.nexters.jjanji.domain.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParticipationDaoTest {

    @Autowired ParticipationDao participationDao;
    @Autowired ParticipationRepository participationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChallengeRepository challengeRepository;

    @Test
    @DisplayName("참여자의 이전, 다음 participation_id 를 조회한다.")
    void getPreviousAndNextPostIds() {
        // given
        Participation participation1 = Participation.builder()
                .member(memberRepository.getReferenceById(1L))
                .challenge(challengeRepository.getReferenceById(10L))
                .goalAmount(100L)
                .build();

        Participation participation2 = Participation.builder()
                .member(memberRepository.getReferenceById(2L))
                .challenge(challengeRepository.getReferenceById(10L))
                .goalAmount(100L)
                .build();

        Participation participation3 = Participation.builder()
                .member(memberRepository.getReferenceById(1L))
                .challenge(challengeRepository.getReferenceById(10L))
                .goalAmount(100L)
                .build();

        Participation participation4 = Participation.builder()
                .member(memberRepository.getReferenceById(1L))
                .challenge(challengeRepository.getReferenceById(10L))
                .goalAmount(100L)
                .build();

        participationRepository.saveAll(
                List.of(participation1, participation2, participation3, participation4)
        );

        //when
        // 3번 participation 의 이전, 다음 participation_id 를 조회한다.
        PrevAndNextPostVO prevAndNextPostVO = participationDao.getPreviousAndNextPostIds(1L, 3L);
        Assertions.assertThat(prevAndNextPostVO.getPreviousPostId()).isEqualTo(1L);
        Assertions.assertThat(prevAndNextPostVO.getNextPostId()).isEqualTo(4L);

    }
}