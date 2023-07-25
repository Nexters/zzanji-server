package com.nexters.jjanji.domain.challenge.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpendingSaveDto {

    @NotEmpty
    private String title;
    private String memo;
    @Min(value = 1)
    private Long spendAmount;
}
