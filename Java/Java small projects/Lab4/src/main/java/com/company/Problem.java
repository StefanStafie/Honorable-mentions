package com.company;
import java.util.List;
import java.util.Map;

public class Problem{
    private Map<Hospital, List<Resident>> hospitalPreferences;
    private Map<Resident, List<Hospital>> residentPreferences;

    public Problem() {
    }

    public Problem(Map<Hospital, List<Resident>> hospitalPreferences, Map<Resident, List<Hospital>> residentPreferences) {
        this.hospitalPreferences = hospitalPreferences;
        this.residentPreferences = residentPreferences;
    }

    public Map<Resident, List<Hospital>> getResidentPreferences() {
        return residentPreferences;
    }
    public Map<Hospital, List<Resident>> getHospitalPreferences() {
        return hospitalPreferences;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "hospitalPreferences=" + hospitalPreferences +
                ", \n\nresidentPreferences=" + residentPreferences +
                '}';
    }
}