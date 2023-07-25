package com.nexters.jjanji.global.exception.exhandler;

import com.nexters.jjanji.global.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class BaseExControllerAdvice extends ResponseEntityExceptionHandler {
    private final String argumentNotValidExMessage = "요청 파라미터 조건이 맞지 않습니다.";

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> baseExceptionHandler(BaseException e, HttpServletRequest request){
        log.warn("[BaseException 발생] request url: {}", request.getRequestURI(), e);
        return ResponseEntity.status(e.getHttpStatus()).body(new ExceptionResponse(e.getMessage()));
    }

    //MethodArgumentNotValidException 는 주로 @ExceptionHandler 으로 처리(유효성 검사 예외)
    //BindException 는 주로 각 컨트롤러에서 처리(바인딩+유효성 검사 포괄 예외)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        //현재는 필드 별 메시지 처리하지 않음. 하나의 예외 메시지로 처리
        //TODO: 메시지 리펙토링 필요
        log.warn("[ArgumentNotValidEx 발생] request url: {}", requestURI, ex);
        return ResponseEntity.status(status).body(new ExceptionResponse(argumentNotValidExMessage));
    }

}
