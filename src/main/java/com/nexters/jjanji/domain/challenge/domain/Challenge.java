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
import java.time.temporal.WeekFields;

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

    @Column(nullable = false)
    private Long month;

    @Column(nullable = false)
    private Long week;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ChallengeState state;

    @Builder
    public Challenge(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.month = calculateChallengeMonth(startAt);
        this.week = calculateChallengeWeek(startAt);
        this.state = ChallengeState.PRE_OPENED;
    }

    private Long calculateChallengeMonth(LocalDateTime startAt) {
        // ISO-8601
        return (long) startAt.get(WeekFields.ISO.weekOfMonth());
    }

    private Long calculateChallengeWeek(LocalDateTime startAt) {
        // ISO-8601
        return (long) startAt.getDayOfMonth() / 7 + 1;
    }

    public void closeChallenge() {
        state = ChallengeState.CLOSED;
    }

    public void openChallenge() {
        state = ChallengeState.OPENED;
    }

    public Boolean isOpenedChallenge(){
        return state.equals(ChallengeState.OPENED);
    }

}
