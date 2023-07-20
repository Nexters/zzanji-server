package com.nexters.jjanji.domain.challenge.dto;

import lombok.Getter;

@Getter
public class PrevAndNextPostVO {
    private final Long previousPostId;
    private final Long nextPostId;

    private PrevAndNextPostVO(Long previousPostId, Long nextPostId) {
        this.previousPostId = previousPostId;
        this.nextPostId = nextPostId;
    }

    public static PrevAndNextPostVO queryResultToObject(Object[] queryResult) {
        Long previousPostId = queryResult[0] != null ? ((Number) queryResult[0]).longValue() : null;
        Long nextPostId = queryResult[1] != null ? ((Number) queryResult[1]).longValue() : null;
        return new PrevAndNextPostVO(previousPostId, nextPostId);
    }
}
