package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class AlreadyParticipateException extends BaseException {
    public AlreadyParticipateException() {
        super(HttpStatus.BAD_REQUEST, "이미 다음 챌린지에 참여중입니다.");
    }
}
