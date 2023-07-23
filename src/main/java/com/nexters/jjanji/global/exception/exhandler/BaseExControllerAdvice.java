package com.nexters.jjanji.global.exception.exhandler;

import com.nexters.jjanji.global.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class BaseExControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> baseExceptionHandler(BaseException e, HttpServletRequest request){
        log.warn("[BaseException 발생] request url: {}", request.getRequestURI(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(new ExceptionResponse(e.getMessage()));
    }

}
