package com.nexters.jjanji.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotExistPlanException extends BaseException {

    public NotExistPlanException(Long planId) {
        super(HttpStatus.NOT_FOUND, String.format("해당 카테고리가 존재하지 않습니다. Id = {%d}", planId));
    }
}
