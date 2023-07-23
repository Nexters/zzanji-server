package com.nexters.jjanji.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PlanNotFoundException extends BaseException{

    public PlanNotFoundException(Long planId) {
        super(
                HttpStatus.NOT_FOUND,
                String.format("카테고리 정보가 존재하지 않습니다. planId = {%d}", planId)
        );

    }
}
