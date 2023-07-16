package com.nexters.jjanji.domain.challenge.domain;

import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ChallengeState state;

    @Builder
    public Challenge(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.state = ChallengeState.NOT_OPENED;
    }

    public void closeChallenge() {
        if (state != ChallengeState.OPENED) {
            throw new RuntimeException("진행중이지 않은 챌린지는 종료될 수 없습니다.");
        }
        state = ChallengeState.CLOSED;
    }

    public void openChallenge() {
        if (state != ChallengeState.NOT_OPENED) {
            throw new RuntimeException("진행중이지 않은 챌린지만 시작될 수 있습니다.");
        }
        state = ChallengeState.OPENED;
    }

}
