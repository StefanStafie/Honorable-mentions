package com.company;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface Algorithm {

    double solve(Knapsack backpack, List<Item> items);
}
