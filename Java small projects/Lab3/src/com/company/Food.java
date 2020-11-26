package com.company;

public class Food implements Item {
    private String name;
    private int weight; // → getWeight, getValue

    public Food() {
    }

    public Food(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public double getValue() {
        return weight * 2;
    }

    @Override
    public String toString() {
        return "\nFood{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", value=" + getValue() +
                ", profit=" + profitFactor() +
                '}';
    }
}
