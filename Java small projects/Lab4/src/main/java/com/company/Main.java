package com.company;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        //Compulsory compulsory = new Compulsory();

        /*generarea problemei*/
        ProblemGenerator gen = new ProblemGenerator(10, 10);
        Problem prob = new Problem(gen.getHospitalPreferences(), gen.getResidentPreferences());

        /*generarea unui Matching (solutie)*/
        Algorithm alg = new Algorithm();
        Matching match = alg.solveOptional(prob);
        System.out.println("Solutia oferita de algoritm este:" +match);

        /*Verificarea solutiei*/
        CheckStable check = new CheckStable();
        System.out.println("Solutia este: " + ((check.check(match, prob))? "corecta":"gresita"));
    }

}
