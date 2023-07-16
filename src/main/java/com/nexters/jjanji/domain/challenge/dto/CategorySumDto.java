package com.nexters.jjanji.domain.challenge.dto;

import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategorySumDto {
    private final PlanCategory category;
    private final Long sum;

    @Builder
    public CategorySumDto(PlanCategory category, Long sum) {
        this.category = category;
        this.sum = sum;
    }
}
