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
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetail;
import com.nexters.jjanji.domain.challenge.dto.response.SpendingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SpendingHistoryService {
    private final ChallengeRepository challengeRepository;
    private final PlanRepository planRepository;
    private final SpendingHistoryRepository spendingHistoryRepository;
    @Transactional
    public void addSpendingHistory(Long planId, SpendingSaveDto dto){
        Challenge findChallenge = getChallengeByPlanIdOrThrow(planId);
        checkOpenChallengeAndThrow(findChallenge);

        Plan findPlan = getPlanOrThrow(planId);
        SpendingHistory createSpending = SpendingHistory.builder()
                .title(dto.getTitle())
                .memo(dto.getMemo())
                .spendAmount(dto.getSpendAmount())
                .plan(findPlan)
                .build();
        spendingHistoryRepository.save(createSpending);

        findPlan.plusCategorySpendAmount(dto.getSpendAmount());
    }

    public SpendingDetailResponse findSpendingList(Long planId, Long cursorId, Pageable pageable){
        Plan findPlan = getPlanOrThrow(planId);

        Slice<SpendingHistory> spendingList = spendingHistoryRepository.findCursorSliceByPlan(findPlan, cursorId, pageable);

        List<SpendingDetail> spendingDetailsDtos = spendingList.getContent().stream()
                .map(sp -> SpendingDetail.from(sp))
                .collect(Collectors.toList());

        return new SpendingDetailResponse(findPlan.getCategoryGoalAmount(), findPlan.getCategorySpendAmount(), spendingList.hasNext(), spendingDetailsDtos);
    }

    @Transactional
    public void editSpendingHistory(Long planId, Long spendingId, SpendingEditDto dto){
        Challenge findChallenge = getChallengeByPlanIdOrThrow(planId);
        checkOpenChallengeAndThrow(findChallenge);

        Plan findPlan = getPlanOrThrow(planId);

        //소비 내역 수정
        SpendingHistory findSpending = getSpendingHistoryOrThrow(spendingId);
        final Long newCategorySpendAmount = findPlan.getCategorySpendAmount() - findSpending.getSpendAmount() + dto.getSpendAmount();
        findSpending.updateSpending(dto.getTitle(), dto.getMemo(), dto.getSpendAmount());

        //카테고리 필드 업데이트
        findPlan.updateCategorySpendAmount(newCategorySpendAmount);
    }

    private Plan getPlanOrThrow(Long planId){
        return planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
    }
    private Challenge getChallengeByPlanIdOrThrow(Long planId){
        return challengeRepository.findChallengeByPlanId(planId)
                .orElseThrow(() -> new PlanChallengeNotFoundException(planId));
    }
    private SpendingHistory getSpendingHistoryOrThrow(Long spendingId){
        return spendingHistoryRepository.findById(spendingId)
                .orElseThrow(() -> new SpendingNotFoundExcpetion(spendingId));
    }
    private void checkOpenChallengeAndThrow(Challenge challenge){
        if(!challenge.isOpenedChallenge()){
            throw new SpendingPeriodInvalidException();
        }
    }
}

