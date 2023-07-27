package com.nexters.jjanji.domain.challenge.domain;

import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
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
