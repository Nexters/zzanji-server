package com.nexters.jjanji.domain.challenge.domain;

import com.nexters.jjanji.domain.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Challenge challenge;

    @Column(nullable = false)
    private Long goalAmount;

    @Column(nullable = false)
    private Long currentAmount = 0L;

    @Builder
    public Participation(Member member, Challenge challenge, Long goalAmount) {
        this.member = member;
        this.challenge = challenge;
        this.goalAmount = goalAmount;
    }

    public void updateCurrentAmount(Long amount) {
        this.currentAmount += amount;
    }

    public void updateGoalAmount(Long goalAmount) {
        this.goalAmount = goalAmount;
    }
}
