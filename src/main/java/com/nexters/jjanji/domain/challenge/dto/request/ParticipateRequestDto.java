package com.nexters.jjanji.domain.challenge.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipateRequestDto {

    @NotNull
    private Long goalAmount;

    @Builder
    public ParticipateRequestDto(Long goalAmount) {
        this.goalAmount = goalAmount;
    }
}
