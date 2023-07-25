package com.nexters.jjanji.domain.challenge.domain;

import com.nexters.jjanji.global.domain.BaseTime;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SpendingHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spending_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Plan plan;

    private String title;

    private String memo;

    @Column(nullable = false)
    private Long spendAmount;

    @Builder
    public SpendingHistory(Plan plan, String title, String memo, Long spendAmount){
        this.plan = plan;
        this.title = title;
        this.memo = memo;
        this.spendAmount = spendAmount;
    }

    public void updateSpending(String title, String memo, Long spendAmount){
        this.title = title;
        this.memo = memo;
        this.spendAmount = spendAmount;
    }
}
