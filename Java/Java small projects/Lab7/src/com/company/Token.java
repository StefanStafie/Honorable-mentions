package com.company;

import java.util.Random;

public class Token {
    int number;

    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }

    public Token(int number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "Token{" +
                "number=" + number +
                '}';
    }
}
