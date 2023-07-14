package com.nexters.jjanji.global.exception;

public class NotExistPlan extends RuntimeException{
    public NotExistPlan() {
        super();
    }

    public NotExistPlan(String message) {
        super(message);
    }

    public NotExistPlan(String message, Throwable cause) {
        super(message, cause);
    }
}
