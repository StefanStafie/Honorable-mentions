package com.example.demo;

public class ExceptionMsg{
    private String message;

    public ExceptionMsg(String msg){
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}