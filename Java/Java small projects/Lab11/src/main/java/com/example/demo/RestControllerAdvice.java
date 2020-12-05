package com.example.demo;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class WebRestControllerAdvice {

    @ExceptionHandler(MyRestException.class)
    public ExceptionMsg handleNotFoundException(ExceptionMsg ex) {
        ExceptionMsg responseMsg = new ExceptionMsg(ex.getMessage());
        return responseMsg;
    }
}
