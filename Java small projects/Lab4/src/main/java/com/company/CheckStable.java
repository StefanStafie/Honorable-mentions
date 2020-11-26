package com.company;

import java.util.List;
import java.util.Map;

public class CheckStable {
    public CheckStable() {}

    /**
     * @param prob the instance of the problem that has been solved
     * @param sol a solution for the problem that needs to be checked
     * */
    public boolean check(Matching sol, Problem prob){
        Map<Hospital, List<Resident>> hospitalPreferences =  prob.getHospitalPreferences();
        Map<Resident, List<Hospital>> residentPreferences = prob.getResidentPreferences();
        //List<Resident> rest = sol.getHospitalResidents();
        /*For each hospital, checks if there is a resident that the Hospital preferred more.
         *For each of these Residents, checks if they wanted the Hospital more than the current Hospital
         * if there is a resident that wanted the Hospital more than the current Resident, it is not stable*/
        for (Map.Entry<Hospital, List<Resident>> hospitalListEntry: hospitalPreferences.entrySet()) {
            /*returns the residents of the current Hospital from matching*/
            Hospital h = hospitalListEntry.getKey();
            List<Resident> rest = sol.getHospitalResidents(h);
            for (Resident r: rest) {
                int rRank = residentPreferences.get(r).indexOf(h);//the ranking of the hospital for the resident (what the resident thinks about the hospital)
                int hRank = hospitalPreferences.get(h).indexOf(r);//the ranking of the resident for the hospital (what the hospital thinks about the resident)
                /*iterate through hospitals that the resident wants better*/
                for (int i = 0; i < rRank; i++) {
                    List<Resident> lastPref = sol.getHospitalResidents(residentPreferences.get(r).get(i));
                    if(lastPref.size() > 0) {
                        Resident lastResident = lastPref.get(lastPref.size() - 1); // the last resident in a hospital i
                        if (hospitalPreferences.get(residentPreferences.get(r).get(i)).contains(r)) {
                            if (hRank > hospitalPreferences.get(residentPreferences.get(r).get(i)).indexOf(lastResident))
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
