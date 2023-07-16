package com.nexters.jjanji.domain.challenge.dto.response;

import com.nexters.jjanji.domain.challenge.dto.CategorySumDto;
import com.nexters.jjanji.domain.challenge.specification.ChallengeState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
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
    private List<CategorySumDto> categorySumList;

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

    public void setCategorySumList(List<CategorySumDto> categorySumList) {
        this.categorySumList = categorySumList;
    }

}
