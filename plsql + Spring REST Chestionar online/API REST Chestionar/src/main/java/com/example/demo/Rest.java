package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RestController
public class Rest {
    DatabaseController databaseController = new DatabaseController();

    @PostMapping("/rest/api1")
    public String api1(@RequestParam(value = "email") String email) throws NoSuchAlgorithmException {
        if (null == email) {
            return "no email, no game";
        }
        LocalDateTime now = LocalDateTime.now(); // de aici timpul

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                (email+now).getBytes(StandardCharsets.UTF_8)); //aici concatenez timpul cu emailul pt hash
        String hash = Base64.getEncoder().encodeToString(encodedhash);
        boolean response = new DatabaseController().addPlayer(email, hash);
        if(response)
            return hash;
        else{
            return "database problem, please try again";
        }
    }
    @PostMapping("/rest/api2")
    public String api2(@RequestParam(value = "email") String email, @RequestParam(value = "hash") String hash, @RequestParam(value = "csv",  defaultValue = "unknown") String csv) throws NoSuchAlgorithmException {
        if (null == email || null == hash) {
            return "no email, no hash, no game";
        }
        hash = hash.replaceAll(" ", "+");
        boolean response = new DatabaseController().playerExists(email, hash);
        String urmatoarea_intrebare = null;
        if(response)
            if(csv.equals("unknown"))
                urmatoarea_intrebare = new DatabaseController().raspunsBazaDate(email, null);
            else
                urmatoarea_intrebare = new DatabaseController().raspunsBazaDate(email, csv);
        else{
            return "email or hash invalid";
        }

        if(urmatoarea_intrebare == null){
            return "csv invalid";
        }
        return urmatoarea_intrebare;
    }


    @GetMapping("/rest/api2")
    public List<Raspunsuri> api2final(@RequestParam(value = "email") String email) {
        if (null == email) {
            return null;
        }
        List<Raspunsuri>raspunsuris = new DatabaseController().raspunsFinal(email);
        System.out.println(raspunsuris.toString());
        return raspunsuris;
    }
}
