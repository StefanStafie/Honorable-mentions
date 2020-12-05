package com.company;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.IntStream;

public class ProblemGenerator {
    private Map<Hospital, List<Resident>> hospitalPreferences = new HashMap<>();
    private Map<Resident, List<Hospital>> residentPreferences = new HashMap<>();

    /**
     * @param hospitalNumber the number of hospitals to be generated
     * @param residentNumber  the number of residents to be generated
     * The algorithm generates hospitals and residents using Faker library for showcase naming
     */
    public ProblemGenerator(int hospitalNumber, int residentNumber) {
        Faker faker = new Faker(); // Initialise faker variable

        /*create Resident list*/
        Resident[] r = IntStream.rangeClosed(0, residentNumber-1)
                .mapToObj(i -> new Resident(faker.name().fullName()))
                .toArray(Resident[]::new);
        List<Resident> residentList = new ArrayList<>();
        for (Resident res : r) {
            residentList.add(res);
        }
        /*create hospital list*/
        Hospital[] h = IntStream.rangeClosed(0, hospitalNumber-1)
                .mapToObj(i -> new Hospital(faker.company().name(), residentNumber/2) )
                .toArray(Hospital[]::new);
        List<Hospital> hospitalList = new ArrayList<>();
        for (Hospital res : h) {
            hospitalList.add(res);
        }

        /*fill hospitalPreferences*/
        Random random = new Random();
        for (Hospital hospital:h) {
            List<Resident> prefList = new ArrayList<>();
            for (Resident resident: r) {
                if(random.nextBoolean())
                    prefList.add(resident);
            }
            hospitalPreferences.put(hospital, prefList);
        }
        /*fill residentPreferences*/
        for (Resident resident:r) {
            List<Hospital> prefList = new ArrayList<>();
            for (Hospital hospital: h) {
                if(random.nextBoolean())
                    prefList.add(hospital);
            }
            residentPreferences.put(resident, prefList);
        }
    }

    public Map<Hospital, List<Resident>> getHospitalPreferences() {
        return hospitalPreferences;
    }

    public Map<Resident, List<Hospital>> getResidentPreferences() {
        return residentPreferences;
    }
}
