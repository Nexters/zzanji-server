package com.nexters.jjanji.domain.challenge.dto.response;

import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpendingDetail {

    private Long spendingId;
    private String title;
    private String memo;
    private Long spendAmount;

    public static SpendingDetail from(SpendingHistory spendingHistory){
        SpendingDetail spendingDetail = new SpendingDetail();
        spendingDetail.spendingId = spendingHistory.getId();
        spendingDetail.title = spendingHistory.getTitle();
        spendingDetail.memo = spendingHistory.getMemo();
        spendingDetail.spendAmount = spendingHistory.getSpendAmount();
        return spendingDetail;
    }
}
