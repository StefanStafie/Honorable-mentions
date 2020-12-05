package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matching {
    Map<Resident, Hospital> solution = new HashMap<>();

    public Matching() {}
    public void addPair(Resident r, Hospital h){
        solution.put(r,h);
    }
    public void removePair(Resident r){
        solution.remove(r);
    }

    public List<Resident> getHospitalResidents(Hospital hospital) {
        List<Resident> residents = new ArrayList<>();
        for (Map.Entry<Resident,Hospital> entry : solution.entrySet()) {
            if(entry.getValue() == hospital)
                residents.add(entry.getKey());
        }
        return residents;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "solution=" + solution +
                '}';
    }
}
