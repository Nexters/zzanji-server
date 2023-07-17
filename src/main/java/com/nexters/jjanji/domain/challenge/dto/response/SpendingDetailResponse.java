package com.nexters.jjanji.domain.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpendingDetailResponse {
    private Long goalAmount;
    private Long spendAmount;
    private List<SpendingDetail> spendingList;
}
