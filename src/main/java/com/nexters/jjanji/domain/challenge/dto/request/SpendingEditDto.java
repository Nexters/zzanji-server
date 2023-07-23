package com.nexters.jjanji.domain.challenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpendingEditDto {

    private String title;
    private String memo;
    private Long spendAmount;
}
