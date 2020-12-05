package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

class MyJWT {
    private String tok;
    public boolean check(String token) {
        try {
            this.tok = token;
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new MyRestException("Invalid token haha");
        }
        return true;
    }

    public Player decodePlayer(){
        try {
            DecodedJWT jwt = JWT.decode(this.tok);
            return new Player(0, jwt.getClaim("name").asString());

        } catch (JWTDecodeException exception) {
            throw new MyRestException("Invalid parameters for player haha");
        }
    }

    public Game decodeGame(){
        try {
            DecodedJWT jwt = JWT.decode(this.tok);
            return new Game( jwt.getClaim("player1").asInt(), jwt.getClaim("player2").asInt(), jwt.getClaim("content").asString(), jwt.getClaim("result").asString());
        } catch (JWTDecodeException exception) {
            throw new MyRestException("Invalid parameters for game haha");
        }
    }

    public String decodeOldName(){
        try {
            DecodedJWT jwt = JWT.decode(this.tok);
            return jwt.getClaim("oldName").asString();

        } catch (JWTDecodeException exception) {
            throw new MyRestException("Invalid parameters for player haha");
        }
    }
    public String decodeNewName(){
        try {
            DecodedJWT jwt = JWT.decode(this.tok);
            return jwt.getClaim("newName").asString();

        } catch (JWTDecodeException exception) {
            throw new MyRestException("Invalid parameters for player haha");
        }
    }
}
