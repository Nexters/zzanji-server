package com.nexters.jjanji.global.exception;

import org.springframework.http.HttpStatus;

public class NotParticipateException extends BaseException {
    public NotParticipateException() {
        super(HttpStatus.BAD_REQUEST, "아직 챌린지에 참여하지 않았습니다.");
    }
}
