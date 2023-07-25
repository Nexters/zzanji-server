package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class SpendingNotFoundExcpetion extends BaseException{

    public SpendingNotFoundExcpetion(Long spendingNo){
        super(
                HttpStatus.BAD_REQUEST,
                String.format("소비내역 정보가 존재하지 않습니다. spendingId = {%d}", spendingNo)
        );
    }
}
