package com.nexters.jjanji.global.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException{

    private final String message;

    protected BaseException(String message) {
        super();
        this.message = message;
    }
}
