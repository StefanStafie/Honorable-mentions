package com.example.demo;

import java.sql.Date;

public class Game {
    int id;
    int player1;
    int player2;
    String content;
    Date date;
    String result;

    public Game(int player1, int player2, String content, String result) {
        this.player1 = player1;
        this.player2 = player2;
        this.content = content;
        this.result = result;
    }

    public Game(int id, int player1, int player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Game(int id, int player1, int player2, String content, Date date, String result) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.content = content;
        this.date = date;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer1() {
        return player1;
    }

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public int getPlayer2() {
        return player2;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
