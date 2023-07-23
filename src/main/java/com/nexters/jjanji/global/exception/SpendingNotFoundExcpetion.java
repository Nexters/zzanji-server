package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class SpendingNotFoundExcpetion extends BaseException{

    public SpendingNotFoundExcpetion(Long spendingNo){
        super(
                String.format("해당 소비내역이 존재하지 않습니다. spendId = {%d}", spendingNo),
                "소비 내역 정보가 존재하지 않습니다.",
                HttpStatus.BAD_REQUEST
        );
    }
}
