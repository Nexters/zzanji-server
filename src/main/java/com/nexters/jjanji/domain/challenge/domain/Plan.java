package com.nexters.jjanji.domain.challenge.domain;

import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
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
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Participation participation;

    @Column(nullable = false)
    private PlanCategory category;

    @Column(nullable = false)
    private Long categoryGoalAmount;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long categorySpendAmount;

    @Builder
    public Plan(Participation participation, PlanCategory category, Long categoryGoalAmount) {
        this.participation = participation;
        this.category = category;
        this.categoryGoalAmount = categoryGoalAmount;
        this.categorySpendAmount = 0L;
    }

    public void plusCategorySpendAmount(Long amount){
        this.categorySpendAmount += amount;
    }
    public void updateCategorySpendAmount(Long Amount){this.categorySpendAmount = Amount;}
}
