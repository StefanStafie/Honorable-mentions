package com.company;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Greedy implements Algorithm {
    public Greedy() {
    }


    @Override
    public double solve(Knapsack backpack, List<Item> items) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item i1, Item i2) {
                return Double.compare(i2.profitFactor(), i1.profitFactor());
            }
        });

        for (Item i: items) {
            if (backpack.getCapacity() < backpack.getLoad() + i.getWeight())
                break;
            backpack.addItem(i);
        }

        double result = 0;
        for (Item i: backpack.items) {
            result += i.getValue();
        }
        return result;
    }
}
