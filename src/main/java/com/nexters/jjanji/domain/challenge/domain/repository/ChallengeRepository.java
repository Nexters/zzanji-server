package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("select c from Challenge c order by c.id desc limit 1")
    Challenge findNextChallenge();

    Optional<Challenge> findChallengeByState(ChallengeState state);

    @Query("select distinct c " +
            "from Plan pl " +
            "join pl.participation pa on pl.id=:planId " +
            "join pa.challenge c")
    Optional<Challenge> findChallengeByPlanId(Long planId);
}
