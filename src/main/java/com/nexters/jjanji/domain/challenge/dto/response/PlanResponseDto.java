package com.nexters.jjanji.domain.challenge.dto.response;

import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class PlanResponseDto {
    private Long planId;
    private PlanCategory category;
    private Long categoryGoalAmount;
    private Long categorySpendAmount;

    public PlanResponseDto(Long planId, PlanCategory category, Long categoryGoalAmount, Long categorySpendAmount) {
        this.planId = planId;
        this.category = category;
        this.categoryGoalAmount = categoryGoalAmount;
        this.categorySpendAmount = categorySpendAmount;
    }
}
