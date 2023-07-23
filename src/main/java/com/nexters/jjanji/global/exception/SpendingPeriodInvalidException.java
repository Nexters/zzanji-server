package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class SpendingPeriodInvalidException extends BaseException{

    public SpendingPeriodInvalidException(Long spendingId){
        super(
                HttpStatus.BAD_REQUEST,
                String.format("소비 내역 추가 및 변경이 가능한 기간이 아닙니다. spendingId = {%d}", spendingId)
        );
    }
}
