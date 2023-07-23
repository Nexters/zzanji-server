package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class SpendingPeriodInvalidException extends BaseException{

    public SpendingPeriodInvalidException(Long spendingId){
        super(
                String.format("유효하지 않은 소비 내역 변경 가능 기간, spendingId = {%d}", spendingId),
                "소비 내역 추가 및 변경이 가능한 기간이 아닙니다.",
                HttpStatus.BAD_REQUEST
        );
    }
}
