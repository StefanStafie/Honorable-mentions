package com.company;


public class Weapon implements Item {


    public Weapon() {
    }

    private WeaponType type;
    private int weight;
    private double value;

    public Weapon(WeaponType type, int weight, double value) {
        this.type = type;
        this.weight = weight;
        this.value = value;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return type.toString();
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\nWeapon{" +
                "type=" + type.toString() +
                ", weight=" + weight +
                ", value=" + value +
                ", profit=" + profitFactor() +

                '}';
    }
}

