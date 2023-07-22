package com.nexters.jjanji.global.exception;

import lombok.Getter;

@Getter
public class NotExistPlanException extends BaseException {

    public NotExistPlanException(Long planId) {
        super(String.format("해당 카테고리가 존재하지 않습니다. Id = {%d}", planId));
    }
}
