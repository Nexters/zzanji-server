package com.nexters.jjanji.domain.challenge.application;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.domain.SpendingHistory;
import com.nexters.jjanji.domain.challenge.domain.repository.ChallengeRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.PlanRepository;
import com.nexters.jjanji.domain.challenge.domain.repository.SpendingHistoryRepository;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingEditDto;
import com.nexters.jjanji.domain.challenge.dto.request.SpendingSaveDto;
import com.nexters.jjanji.global.exception.PlanNotFoundException;
import com.nexters.jjanji.global.exception.PlanChallengeNotFoundException;
import com.nexters.jjanji.global.exception.SpendingNotFoundExcpetion;
import com.nexters.jjanji.global.exception.SpendingPeriodInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final ChallengeRepository challengeRepository;
    private final PlanRepository planRepository;
    private final SpendingHistoryRepository spendingHistoryRepository;
    @Transactional
    public void addSpendingHistory(Long planId, SpendingSaveDto dto){
        Plan findPlan = planRepository.findById(planId).orElseThrow(() -> new PlanNotFoundException(planId));

        SpendingHistory createSpending = SpendingHistory.builder()
                .title(dto.getTitle())
                .memo(dto.getMemo())
                .spendAmount(dto.getSpendAmount())
                .plan(findPlan)
                .build();
        spendingHistoryRepository.save(createSpending);
    }

    @Transactional
    public Long editSpendingHistory(Long planId, Long spendingId, SpendingEditDto dto){
        //현재 챌린지일 경우에만 소비 내역 수정 가능.
        Challenge findChallenge = challengeRepository.findChallengeByPlanId(planId)
                .orElseThrow(() -> new PlanChallengeNotFoundException(planId));
        if(!findChallenge.isOpenedChallenge()){
            throw new SpendingPeriodInvalidException(spendingId);
        }

        Plan findPlan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        //소비 내역 수정
        SpendingHistory findSpending = spendingHistoryRepository.findById(spendingId)
                .orElseThrow(() -> new SpendingNotFoundExcpetion(spendingId));
        final Long newCategorySpendAmount = findPlan.getCategorySpendAmount() - findSpending.getSpendAmount() + dto.getSpendAmount();
        findSpending.updateSpending(dto.getTitle(), dto.getMemo(), dto.getSpendAmount());

        //카테고리 "categorySpendAmount" 필드 업데이트
        findPlan.updateCategorySpendAmount(newCategorySpendAmount);

        return findSpending.getId();
    }
}

