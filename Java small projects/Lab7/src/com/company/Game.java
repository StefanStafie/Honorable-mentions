package com.company;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Game {
    Player[] players;
    int playerCount;
    int tokenCount;
    int progressionSize;
    Board board;
    List<Player> playerList = new ArrayList<>();

    public Game(int playerCount, int tokenCount, int progressionSize) {
        if(tokenCount < progressionSize * playerCount) {
            System.out.println("tokenCount  should be at least equal to progressionSize * playerCount");
            return;
        }
        this.playerCount = playerCount;
        this.tokenCount = tokenCount;
        this.progressionSize = progressionSize;
        board = new Board(tokenCount);
        play();
    }

    public void play(){
        if(playerCount<2){
            return;
        }
        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new Player("player " + i, progressionSize, tokenCount, board);
        }
        while(!winnerExists()) {
        }
        for (int i = 0; i < playerCount; i++) {
                players[i].setRunning(false);
        }
    }

    private boolean winnerExists() {
        for (int i = 0; i < playerCount; i++) {
            if(!players[i].isRunning()){
                System.out.println("The winner is: " + players[i]);
                return true;
            }
        }
        return false;
    }
}
