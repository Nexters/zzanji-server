package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Participation;
import com.nexters.jjanji.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByMemberAndChallenge(Member member, Challenge nextChallenge);

    Optional<Participation> findByMemberAndChallenge(Member member, Challenge nextChallenge);

    @Modifying
    @Query(value = "INSERT INTO participation (challenge_id, current_amount, goal_amount, member_id)  " +
            "select :nextChallengeId, 0, participation.goal_amount, member_id " +
            "from participation " +
            "join challenge c on c.challenge_id = participation.challenge_id " +
            "where c.challenge_id = :currentChallengeId",
            nativeQuery = true)
    void copyPreviousParticipate(@Param("currentChallengeId") Long currentChallengeId, @Param("nextChallengeId") Long nextChallengeId);
}
