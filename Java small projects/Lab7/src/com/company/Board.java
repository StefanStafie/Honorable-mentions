package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    List<Token> tokens = new ArrayList<>();

    public Board(int tokenCount) {
        for (int i = 0; i < tokenCount; i++) {
            tokens.add(new Token(i));
        }
    }

    /**Players have access to the board and they can call this function
     * when a players calls the function, they will receive a RANDOM token, and the token of the Board will be removed
     * it is synchronised so that only one Player can get a token at a given time
     * */
    public synchronized Token getToken() {
        if(tokens.size() > 0) {
            int number = new Random().nextInt(tokens.size());
            Token aux = tokens.get(number);
            tokens.remove(number);
            return aux;
        }else{
            return null;
        }
    }
    /**when a players calls the function, they will receive a RANDOM token in excange for one of their token
     * it is synchronised so that only one Player can exchange a token at a given time
     * */
    public synchronized Token exchangeToken(Token token) {
        if(tokens.size() > 0) {
            int number = new Random().nextInt(tokens.size());
            Token aux = tokens.get(number);
            tokens.remove(number);
            tokens.add(token);
            return aux;
        }else{
            tokens.add(token);
            return null;
        }
    }
}
