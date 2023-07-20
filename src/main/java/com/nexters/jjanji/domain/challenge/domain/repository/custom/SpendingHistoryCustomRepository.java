package com.nexters.jjanji.domain.challenge.domain.repository.custom;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface SpendingHistoryCustomRepository {
    Slice<SpendingHistory> findOffsetSliceByPlan(Plan plan, Pageable pageable);
    Slice<SpendingHistory> findCursorSliceByPlan(Long lastSpendingId, Plan plan, Pageable pageable);
}
