package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetail;
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetailResponse;
import com.nexters.jjanji.global.exception.NotExistPlanException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final PlanRepository planRepository;
    private final SpendingHistoryRepository spendingHistoryRepository;
    @Transactional
    public void addSpendingHistory(Long planId, SpendingSaveDto dto){
        Plan findPlan = validAndGetPlan(planId);

        SpendingHistory createSpending = SpendingHistory.builder()
                .title(dto.getTitle())
                .memo(dto.getMemo())
                .spendAmount(dto.getSpendAmount())
                .plan(findPlan)
                .build();
        spendingHistoryRepository.save(createSpending);

        findPlan.plusCategorySpendAmount(dto.getSpendAmount());
    }

    public SpendingDetailResponse findSpendingList(Long planId){
        Plan findPlan = validAndGetPlan(planId);

        List<SpendingDetail> spendingDetailsDto = spendingHistoryRepository.findByPlan(findPlan).stream()
                .map(sp -> new SpendingDetail(sp.getTitle(), sp.getMemo(), sp.getSpendAmount()))
                .collect(Collectors.toList());

        return new SpendingDetailResponse(findPlan.getCategoryGoalAmount(), findPlan.getCategorySpendAmount(), spendingDetailsDto);
    }

    private Plan validAndGetPlan(Long planId){
        return planRepository.findById(planId).orElseThrow(() -> new NotExistPlanException(planId));
    }
}

