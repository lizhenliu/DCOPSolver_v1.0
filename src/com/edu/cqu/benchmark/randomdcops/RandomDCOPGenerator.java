package com.edu.cqu.benchmark.randomdcops;

import com.edu.cqu.benchmark.AbstractGraph;

import java.util.Set;

public class RandomDCOPGenerator extends AbstractGraph {
    public RandomDCOPGenerator(String name, int nbAgents, int domainsize, int minCost, int maxCost) {
        super(name, nbAgents, domainsize, minCost, maxCost);
    }

    public void generateInitConnectedGraph(){
        for (int i = 0; i < nbAgents-1; i++) {
            int startPoint;
            int endPoint;
            while (true){
                startPoint = random.nextInt(nbAgents)+1;
                endPoint = random.nextInt(nbAgents)+1;
            }
        }
    }
}
