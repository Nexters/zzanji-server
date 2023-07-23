package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class PlanChallengeNotFoundException extends BaseException{

    public PlanChallengeNotFoundException(Long planId){
        super(
                String.format("카테고리에 해당하는 챌린지 정보가 없습니다. planId = {%d}", planId),
                "카테고리에 해당하는 챌린지 정보가 없습니다.",
                HttpStatus.BAD_REQUEST
        );
    }
}
