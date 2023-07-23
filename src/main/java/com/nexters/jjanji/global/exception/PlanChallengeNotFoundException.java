package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class PlanChallengeNotFoundException extends BaseException{

    public PlanChallengeNotFoundException(Long planId){
        super(
                HttpStatus.BAD_REQUEST,
                String.format("카테고리에 해당하는 챌린지 정보가 없습니다. planId = {%d}", planId)
        );
    }
}
