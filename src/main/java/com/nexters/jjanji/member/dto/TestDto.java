package com.nexters.jjanji.member.dto;

import lombok.Data;

@Data
public class TestDto {
    private Long memberId;

    public TestDto(Long memberId) {
        this.memberId = memberId;
    }
}
