package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("select c from Challenge c order by c.id desc limit 1")
    Challenge findNextChallenge();
    Optional<Challenge> findChallengeByState(ChallengeState state);
}
