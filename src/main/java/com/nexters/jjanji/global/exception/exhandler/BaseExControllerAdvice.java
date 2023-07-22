package com.nexters.jjanji.global.exception.exhandler;

import com.nexters.jjanji.global.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class BaseExControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ExceptionResponse baseExceptionHandler(BaseException e, HttpServletRequest request){
        log.warn("[BaseException 발생] request url: {}", request.getRequestURI(), e);
        return new ExceptionResponse(e.getMessage());
    }

}
