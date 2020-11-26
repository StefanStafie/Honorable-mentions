package com.company;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Algorithm {
    public Algorithm() {}
    /**
     * @param prob the problem to be solved
     * @return Matching, a possible solution to the given problem
     */
    public Matching solveOptional(Problem prob){
        Map<Hospital, List<Resident>> hospitalPreferences = prob.getHospitalPreferences();
        Map<Resident, List<Hospital>> residentPreferences = prob.getResidentPreferences();
        Matching matching = new Matching();

        /*for each resident, picks the most wanted hospital that also wants the resident
        * first resident come, first served
        */
        for (Map.Entry<Resident,List<Hospital>> entry : residentPreferences.entrySet()) {
            List<Hospital> possible = entry.getValue().stream()
                    .filter(res -> hospitalPreferences.get(res).contains(entry.getKey()))
                    .collect(Collectors.toList());
            for (int i = 0; i < possible.size(); i++ ) {
                if(possible.get(i).getCapacity() > possible.get(i).getLoad()) {
                    matching.addPair(entry.getKey(), possible.get(i));
                    possible.get(i).setLoad(possible.get(i).getLoad()+1);
                    break;
                }
            }


        }
        return matching;
    }
}
