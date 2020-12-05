package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Rest {
    DatabaseController databaseController = new DatabaseController();

    @GetMapping("/rest")
    public List<Player> getPlayers(@RequestParam(value = "jwt") String token) {
        if (null == token) {
            throw new MyRestException("No jwt found haha");
        }
        MyJWT myJWT = new MyJWT();
        if(!myJWT.check(token))
            throw new MyRestException("Bad secret haha");

        List<Player> players = databaseController.getPlayers();
        if (players == null) {
            throw new MyRestException("Could not retrieve players haha");
        }
        return players;
    }

    @GetMapping("/rest/game")
    public List<Game> getGames(@RequestParam(value = "jwt") String token) {
        if (null == token) {
            throw new MyRestException("No jwt found haha");
        }

        MyJWT myJWT = new MyJWT();
        if(!myJWT.check(token))
            throw new MyRestException("Bad secret");

        List<Game> games = databaseController.getGames();
        if (games == null) {
            throw new MyRestException("Could not retrieve games haha");
        }
        return games;
    }


    @PostMapping("/rest")
    public boolean PostPlayer(@RequestParam(value = "jwt") String token) {
        if (null == token) {
            throw new MyRestException("No jwt found haha");
        }

        MyJWT myJWT = new MyJWT();
        if(!myJWT.check(token))
            throw new MyRestException("Bad secret haha");
        boolean response = databaseController.addPlayer(myJWT.decodePlayer());
        if (false == response) {
            throw new MyRestException("Could not insert player haha");
        }
        return true;
    }

    @PostMapping("/rest/game")
    public boolean PostGame(@RequestParam(value = "jwt") String token) {
        if (null == token) {
            throw new MyRestException("No jwt found haha");
        }
        MyJWT myJWT = new MyJWT();
        if(!myJWT.check(token))
            throw new MyRestException("Bad secret haha");
        boolean response = databaseController.insertGame(myJWT.decodeGame());
        if (false == response) {
            throw new MyRestException("Could not insert game haha");
        }
        return true;
    }

    @PutMapping("/rest")
    public boolean modifyPlayerName(@RequestParam(value = "jwt") String token) {
        if (null == token) {
            throw new MyRestException("No jwt found haha");
        }
        MyJWT myJWT = new MyJWT();
        if(!myJWT.check(token))
            throw new MyRestException("Bad secret");
        boolean response = databaseController.modifyName(myJWT.decodeNewName(),myJWT.decodeOldName());
        if (false == response) {
            throw new MyRestException("Could not modify name of player haha " + myJWT.decodeOldName());
        }
        return true;
    }

    @DeleteMapping("/rest")
    public boolean deletePlayer(@RequestParam(value = "jwt") String token) {
        if (null == token) {
            throw new MyRestException("No jwt found haha");
        }
        MyJWT myJWT = new MyJWT();
        if(!myJWT.check(token))
            throw new MyRestException("Bad secret haha");
        boolean response = databaseController.deletePlayer(myJWT.decodePlayer());
        if (false == response) {
            throw new MyRestException("Could not delete player  haha " + "name");
        }
        return true;
    }


}
