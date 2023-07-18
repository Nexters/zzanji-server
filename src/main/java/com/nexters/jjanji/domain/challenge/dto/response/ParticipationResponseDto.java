package com.nexters.jjanji.domain.challenge.dto.response;

import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import com.nexters.jjanji.domain.challenge.specification.PlanCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ParticipationResponseDto {

    private final Long challengeId;
    private final LocalDateTime endAt;
    private final LocalDateTime startAt;
    private final Long month;
    private final Long week;
    private final ChallengeState state;
    private final Long participationId;
    private final Long goalAmount;
    private final Long currentAmount;
    private final List<PlanResponseDto> planList = new ArrayList<>();

    static class PlanResponseDto {
        private final Long planId;
        private final PlanCategory category;
        private final Long categoryGoalAmount;
        private final Long categorySpendAmount;

        public PlanResponseDto(Long planId, PlanCategory category, Long categoryGoalAmount, Long categorySpendAmount) {
            this.planId = planId;
            this.category = category;
            this.categoryGoalAmount = categoryGoalAmount;
            this.categorySpendAmount = categorySpendAmount;
        }
    }

    @QueryProjection
    public ParticipationResponseDto(Long challengeId, LocalDateTime endAt, LocalDateTime startAt, Long month, Long week, ChallengeState state, Long participationId, Long goalAmount, Long currentAmount) {
        this.challengeId = challengeId;
        this.endAt = endAt;
        this.startAt = startAt;
        this.month = month;
        this.week = week;
        this.state = state;
        this.participationId = participationId;
        this.goalAmount = goalAmount;
        this.currentAmount = currentAmount;
    }

    public void setPlanList(List<Plan> planList) {
        for (Plan plan : planList) {
            this.planList.add(new PlanResponseDto(plan.getId(), plan.getCategory(), plan.getCategoryGoalAmount(), plan.getCategorySpendAmount()));
        }
    }
}
