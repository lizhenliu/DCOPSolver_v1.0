package edu.cqu.benchmark.dcop.coloringGraph;

import edu.cqu.benchmark.dcop.randomDCOPs.RandomDCOPsGenerator;

public class ColoringGraphGenerator extends RandomDCOPsGenerator {


    public ColoringGraphGenerator(String problemType, int instanceId, int nbAgents, int domainSize, int minCost, int maxCost, double density) {
        super(problemType, instanceId, nbAgents, domainSize, minCost, maxCost, density);
    }

    @Override
    public int getRandomCost(int i, int j) {
        return i==j ? 1:0;
    }
}
