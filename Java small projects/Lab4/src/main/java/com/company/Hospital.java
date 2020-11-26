package com.company;

public class Hospital implements Comparable<Hospital>{
    private int capacity;
    private int load = 0;
    private String name;
    public Hospital(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "\nHospital{" +
                "name=" + this.name +
                ", capacity=" + this.capacity +
                '}';
    }

    @Override
    public int compareTo(Hospital hospital) {
        return this.name.compareTo(hospital.getName());
    }
}
