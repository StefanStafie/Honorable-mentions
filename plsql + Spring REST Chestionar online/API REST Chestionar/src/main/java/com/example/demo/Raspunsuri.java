package com.example.demo;

public class Raspunsuri {
    String raspunsCorect;
    String raspunsUser;
    String intrebare;
    String punctaj;

    public Raspunsuri(String raspunsCorect, String raspunsUser, String intrebare, String punctaj) {
        this.raspunsCorect = raspunsCorect;
        this.raspunsUser = raspunsUser;
        this.intrebare = intrebare;
        this.punctaj = punctaj;
    }

    @Override
    public String toString() {
        return "Raspunsuri{" +
                "raspunsCorect='" + raspunsCorect + '\'' +
                ", raspunsUser='" + raspunsUser + '\'' +
                ", intrebare='" + intrebare + '\'' +
                ", punctaj=" + punctaj +
                '}';
    }

    public String getIntrebare() {
        return intrebare;
    }

    public void setIntrebare(String intrebare) {
        this.intrebare = intrebare;
    }

    public String getRaspunsCorect() {
        return raspunsCorect;
    }

    public void setRaspunsCorect(String raspunsCorect) {
        this.raspunsCorect = raspunsCorect;
    }

    public String getRaspunsUser() {
        return raspunsUser;
    }

    public void setRaspunsUser(String raspunsUser) {
        this.raspunsUser = raspunsUser;
    }

    public String getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(String punctaj) {
        this.punctaj = punctaj;
    }
}
