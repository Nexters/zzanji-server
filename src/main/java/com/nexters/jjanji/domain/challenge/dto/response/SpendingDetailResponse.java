package com.nexters.jjanji.domain.challenge.dto.response;

import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpendingDetailResponse {
    private PlanCategory category;
    private Long goalAmount;
    private Long spendAmount;
    private Boolean hasNext;
    private List<SpendingDetail> spendingList;
}
