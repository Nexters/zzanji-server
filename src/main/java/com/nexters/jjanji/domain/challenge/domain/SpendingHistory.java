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

    public static SpendingHistory createSpending(Plan plan, String title, String memo, Long spendAmount){
        SpendingHistory createSpending = new SpendingHistory();
        createSpending.plan = plan;
        createSpending.title = title;
        createSpending.memo = memo;
        createSpending.spendAmount = spendAmount;
        return createSpending;
    }

}
