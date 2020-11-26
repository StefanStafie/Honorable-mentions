package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements  Runnable {
    String name;
    int progressionSize;
    int tokenMax;
    List<Token> playerTokens = new ArrayList<>();
    Board board;
    Thread t;
    public boolean running = true;

    public Player(String name, int progressionSize, int tokenMax, Board board) {
        this.name = name;
        this.progressionSize = progressionSize;
        this.tokenMax = tokenMax;
        this.board = board;
        t = new Thread(this, name);
        System.out.println("New thread: " + t);
        t.start();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        //the player takes progressionSize tokens from the table
        for (int i = 0; i < progressionSize; i++) {
            Token crrToken = board.getToken();
            if (crrToken!=null)
                playerTokens.add(crrToken);
        }
        //while the game is not finished, keep exchanging a player token with a board token
        while(running) {
            if (isProgression()) {
                running = false;
                return;
            }
            Token exchangeToken = playerTokens.get(new Random().nextInt(playerTokens.size()));
            Token receivedToken = board.exchangeToken(exchangeToken);
            if(receivedToken != null)
                playerTokens.add(receivedToken);
            playerTokens.remove(exchangeToken);
        }
    }
    private boolean isProgression(){
        playerTokens.sort((Token t1, Token t2) -> t2.getNumber() - (t1.getNumber()));
        int leap = playerTokens.get(0).getNumber() - playerTokens.get(1).getNumber();
        for (int i = 2; i < playerTokens.size(); i++) {
            if(playerTokens.get(i-1).getNumber() - playerTokens.get(i).getNumber() != leap)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerTokens=" + playerTokens +
                '}';
    }
}
