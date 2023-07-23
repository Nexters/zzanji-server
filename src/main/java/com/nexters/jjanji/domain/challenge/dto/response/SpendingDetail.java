package com.nexters.jjanji.domain.challenge.dto.response;

import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpendingDetail {

    private Long spendingId;
    private String title;
    private String memo;
    private Long spendAmount;

    public static SpendingDetail from(SpendingHistory spendingHistory){
        return new SpendingDetail(spendingHistory.getId(), spendingHistory.getTitle(), spendingHistory.getMemo(), spendingHistory.getSpendAmount());
    }
}
