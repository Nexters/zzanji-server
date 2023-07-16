package com.nexters.jjanji.global.exception.exhandler;

import com.nexters.jjanji.global.exception.NotExistPlanException;
import com.nexters.jjanji.global.exception.dto.BaseExResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class BaseExControllerAdvice {

    @ExceptionHandler(NotExistPlanException.class)
    public BaseExResponse NotExistPlan(NotExistPlanException e, HttpServletResponse response){
        response.setStatus(e.getStatus().value());

        log.info(e.getMessage());
        return new BaseExResponse(e.getShowMessage(), e.getStatus().name());
    }

}
