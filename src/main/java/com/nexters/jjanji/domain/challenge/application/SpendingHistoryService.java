package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.global.exception.NotExistPlan;
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
        Plan findPlan = planRepository.findById(planId).orElseThrow(() -> new NotExistPlan("계획된 카테고리 정보가 없습니다."));
        SpendingHistory createSpending = SpendingHistory.createSpending(findPlan, dto.getTitle(), dto.getMemo(), dto.getSpendAmount());
        spendingHistoryRepository.save(createSpending);
    }
}

