package com.nexters.jjanji.challenge.domain;

import com.nexters.jjanji.challenge.specification.PlanCategory;
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
    private Long goalAmount;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long currentAmount;

}
