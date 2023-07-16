package com.nexters.jjanji.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException{

    private final String showMessage;
    private final HttpStatus status;

    protected BaseException(String logMessage, String showMessage, HttpStatus status) {
        super(logMessage);
        this.showMessage = showMessage;
        this.status = status;
    }
}
