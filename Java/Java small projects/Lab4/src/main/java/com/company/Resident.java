package com.company;

public class Resident implements Comparable<Resident>{
    private String name;
    public Resident(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nResident{" +
                "name='" + this.name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Resident resident) {
        return this.name.compareTo(resident.getName());
    }
}
