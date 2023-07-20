package com.nexters.jjanji.domain.challenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexters.jjanji.domain.challenge.domain.Plan;
import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ParticipationResponseDto {

    private final Long challengeId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime startAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime endAt;
    private final Long month;
    private final Long week;
    private final ChallengeState state;
    private final Long participationId;
    private final Long goalAmount;
    private final Long currentAmount;
    private final List<PlanResponseDto> planList = new ArrayList<>();

    @QueryProjection
    public ParticipationResponseDto(Long challengeId, LocalDateTime startAt, LocalDateTime endAt, Long month, Long week, ChallengeState state, Long participationId, Long goalAmount, Long currentAmount) {
        this.challengeId = challengeId;
        this.startAt = startAt;
        this.endAt = endAt;
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
