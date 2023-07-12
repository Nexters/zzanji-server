package com.nexters.jjanji.domain.challenge.dto.request;

import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryPlanRequestDto {
    private PlanCategory category;
    private Long goalAmount;

    @Builder
    public CreateCategoryPlanRequestDto(PlanCategory category, Long goalAmount) {
        this.category = category;
        this.goalAmount = goalAmount;
    }
}
