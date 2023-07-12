package com.nexters.jjanji.domain.member.dto.response;

import lombok.Data;

@Data
public class TestResponseDto {
    private Long memberId;

    public TestResponseDto(Long memberId) {
        this.memberId = memberId;
    }
}
