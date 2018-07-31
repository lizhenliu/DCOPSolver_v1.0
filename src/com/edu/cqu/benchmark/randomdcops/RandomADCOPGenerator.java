package com.edu.cqu.benchmark.randomdcops;

public class RandomADCOPGenerator extends RandomDCOPGenerator {
    public RandomADCOPGenerator(String name, int nbAgents, int domainsize, int minCost, int maxCost, int density) {
        super(name, nbAgents, domainsize, minCost, maxCost, density);
        type = "ADCOP";
    }

    @Override
    protected String getTuples() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < domainsize; i++) {
            for (int j = 0; j < domainsize; j++) {
                int cost1 = randomCost(i,j);
                int cost2 = randomCost(i,j);
                stringBuilder.append(cost1 + " " + cost2 + ":");
                stringBuilder.append(i + " " + j + "|");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
