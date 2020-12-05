package com.example.demo;

public class MyRestException extends RuntimeException{

    public MyRestException(String msg) {
        super(msg);
    }
}