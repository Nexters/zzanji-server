package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.global.exception.NotExistPlanException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final PlanRepository planRepository;
    private final SpendingHistoryRepository spendingHistoryRepository;
    @Transactional
    public void addSpendingHistory(Long planId, SpendingSaveDto dto){
        Plan findPlan = planRepository.findById(planId).orElseThrow(() -> new NotExistPlanException(planId));

        SpendingHistory createSpending = SpendingHistory.builder()
                .title(dto.getTitle())
                .memo(dto.getMemo())
                .spendAmount(dto.getSpendAmount())
                .plan(findPlan)
                .build();
        spendingHistoryRepository.save(createSpending);
    }
}

