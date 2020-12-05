package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProblemGenerator {
    List<Item> items = new ArrayList<>();
    public ProblemGenerator() {
    }
    public List<Item> generate(int n){
        Random rand = new Random();
        items.clear();
        for (int i = 0; i < n; i++) {
            int rand_int = rand.nextInt(3);
            Item item;
            switch (rand_int) {
                case 0:
                    item = new Book("Book " + i, rand.nextInt(1000)+1, rand.nextDouble()*10+1);
                    break;
                case 1:
                    item = new Food("Food " + i, rand.nextInt(100)+1);
                    break;
                default:
                    item = new Weapon(WeaponType.staff, rand.nextInt(100)+1, rand.nextDouble()*100+1);
                    break;
            }
            items.add(item);
        }
        return items;
    }
    public List<Item> lastGeneration(){
        return items;
    }
}
