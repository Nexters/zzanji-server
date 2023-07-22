package com.nexters.jjanji.domain.challenge.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateGoalAmountRequestDto {
    private Long goalAmount;

    @Builder
    public UpdateGoalAmountRequestDto(Long goalAmount) {
        this.goalAmount = goalAmount;
    }
}
