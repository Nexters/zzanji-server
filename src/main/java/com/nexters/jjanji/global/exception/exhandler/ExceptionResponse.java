package com.nexters.jjanji.global.exception.exhandler;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private final String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
