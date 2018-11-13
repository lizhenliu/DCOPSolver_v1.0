package edu.cqu.benchmark.dcop.coloringGraph;

import edu.cqu.benchmark.dcop.randomDCOPs.RandomDCOPsGenerator;


public class WeightedColoringGraphGenerator extends RandomDCOPsGenerator {

    public WeightedColoringGraphGenerator(String name, String problemType, int instanceId, int nbAgents, int domainSize, int minCost, int maxCost, double density) {
        super(name,problemType, instanceId, nbAgents, domainSize, minCost, maxCost, density);
    }

    @Override
    public int getRandomCost(int i, int j) {
        return i==j ? super.getRandomCost(i, j):0;
    }
}
