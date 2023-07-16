package com.nexters.jjanji.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpendingDetail {

    private String title;
    private String memo;
    private Long spendAmount;

}
