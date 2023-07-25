package com.nexters.jjanji.domain.challenge.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpendingEditDto {

    @NotEmpty
    private String title;
    private String memo;
    @NotNull
    @Min(value = 1)
    private Long spendAmount;
}
