package com.company;

public class Book implements Item{
    private String name;
    private int pageNumber; //→ getWeight
    private double value;

    public Book() {
    }
    public Book(String name, int pageNumber, double value) {
        this.name = name;
        this.pageNumber = pageNumber;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int getWeight() {
        return Integer.max((int)pageNumber/100, 1 );
    }

    @Override
    public String toString() {
        return "\nBook{" +
                "name='" + name + '\'' +
                ", pageNumber=" + pageNumber +
                ", value=" + value +
                ", profit=" + profitFactor() +
                '}';
    }
}
