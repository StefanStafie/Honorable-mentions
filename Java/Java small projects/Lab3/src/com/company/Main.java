package com.company;

public class Main {
    private static final int CAPACITY = 500;
    private static final int ITEMNUMBER = 500;
    private static final int ITERATIONS = 500;

    public static void main(String[] args) {
	    Knapsack pack1 = new Knapsack(  CAPACITY); //Knapsack for dynamic
        Knapsack pack2 = new Knapsack(  CAPACITY); // Knapsack for greedy

        ProblemGenerator g = new ProblemGenerator();
        Greedy greedyAlg = new Greedy();
        Dynamic dynamicAlg = new Dynamic();
        double dyn, gred;
        double dynamicAvg= 0, greedyAvg = 0, dynamicTime = 0, greedyTime = 0; // for calculating performance

        for (int i = 0; i < ITERATIONS; i++) {
            pack1.delete();
            pack2.delete();
            long startTime = System.currentTimeMillis(); //check clock
            dyn = dynamicAlg.solve(pack1, g.generate(ITEMNUMBER));//dynamic algorithm
            long time1 = System.currentTimeMillis();//check clock
            gred = greedyAlg.solve(pack2, g.lastGeneration());//greedy algorithm
            long time2 = System.currentTimeMillis();//check clock
            /*time for calculating avg time*/
            dynamicTime += time1-startTime;
            greedyTime += time2 - time1;
            /*values for calculating avg Knapsack values*/
            dynamicAvg += dyn;
            greedyAvg += gred;
            System.out.println(i);//progress print
        }

        System.out.println("dynamic value avg: " + dynamicAvg/10000 + " greedy value avg: " + greedyAvg/10000); //result difference
        System.out.println("dynamic time avg: " + dynamicTime/10000 + " greedy time avg: " + greedyTime/10000); // time difference
    }
}
