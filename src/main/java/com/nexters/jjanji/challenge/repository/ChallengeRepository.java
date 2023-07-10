package com.nexters.jjanji.challenge.repository;

import com.nexters.jjanji.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
