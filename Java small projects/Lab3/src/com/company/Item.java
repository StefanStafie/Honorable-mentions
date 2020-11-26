package com.company;

public interface Item {
    String getName();
    double getValue();
    int getWeight();
    default double profitFactor() {
        return getValue() / getWeight();
    }

}
