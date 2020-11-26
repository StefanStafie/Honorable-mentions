package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Knapsack {
    private double capacity;
    private double load=0;
    List<Item> items = new ArrayList<>();

    public Knapsack() {  }

    public Knapsack(int capacity) {
        this.capacity = capacity;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addItem(Item item) {
        if(item.getWeight() + load <= capacity){
            items.add(item);
            load = (int) (load + item.getWeight());
        }
    }

    public double getLoad() {
        return load;
    }

    public void print(){
        this.order();
        System.out.println(this.toString());
    }
    public void order(){
        Collections.sort(items, (Item i1, Item i2) ->
                i1.getName().compareTo( i2.getName() ));
    }

    public List<Item> getItems() {

        return items;
    }

    public void delete(){
        items.clear();
        load = 0;
    }
    @Override
    public String toString() {
        return "Knapsack{" +
                "capacity=" + capacity +
                ", items=" + items +
                '}';
    }


}
