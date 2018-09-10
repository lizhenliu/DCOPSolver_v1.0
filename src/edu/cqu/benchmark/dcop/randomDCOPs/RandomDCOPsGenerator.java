package edu.cqu.benchmark.dcop.randomDCOPs;

import edu.cqu.benchmark.dcop.AbstractGraph;

import java.util.HashSet;
import java.util.Set;

public class RandomDCOPsGenerator extends AbstractGraph {

    public double density;
    public RandomDCOPsGenerator(String problemType, int instanceId, int nbAgents, int domainSize, int minCost, int maxCost,double density) {
        super(problemType, instanceId, nbAgents, domainSize, minCost, maxCost);
        this.density = density;
    }

    @Override
    protected void generateConstraint() {
        generateInitialConstraint();
        for (int i = nbAgents - 1; i < (int)(nbAgents*(nbAgents-1)*density/2); i++) {
            int startPoint = random.nextInt(nbAgents)+1;
            int endPoint = random.nextInt(nbAgents)+1;
            while (startPoint == endPoint || adjacentTable.get(startPoint).contains(endPoint)){
                startPoint = random.nextInt(nbAgents)+1;
                endPoint = random.nextInt(nbAgents)+1;
            }
            adjacentTable.get(startPoint).add(endPoint);
            adjacentTable.get(endPoint).add(startPoint);
            source.add(Math.min(startPoint,endPoint));
            dest.add(Math.max(startPoint,endPoint));
            nbConstraints++;
            nbRelations++;
        }
    }

    protected void generateInitialConstraint() {
        for (int i = 0; i < nbAgents - 1; i++) {
            int startPoint;
            int endPoint;
            while (true){
                startPoint = random.nextInt(nbAgents)+1;
                endPoint = random.nextInt(nbAgents)+1;
                Set<Integer> visited = new HashSet<>();
                if (isValidate(endPoint,startPoint,visited)){
                    break;
                }
            }

            Set<Integer> adjacent = adjacentTable.get(startPoint);
            if (adjacent == null){
                adjacent = new HashSet<>();
                adjacentTable.put(startPoint,adjacent);
            }
            adjacent.add(endPoint);
            adjacent = adjacentTable.get(endPoint);
            if (adjacent == null){
                adjacent = new HashSet<>();
                adjacentTable.put(endPoint,adjacent);
            }
            adjacent.add(startPoint);

            source.add(Math.min(startPoint,endPoint));
            dest.add(Math.max(startPoint,endPoint));
            nbConstraints++;
            nbRelations++;
        }
    }

    private boolean isValidate(int nextPoint, int target, Set<Integer> visited) {
        if (nextPoint == target){
            return false;
        }
        visited.add(nextPoint);
        Set<Integer> adjacent = adjacentTable.get(nextPoint);
        if (adjacent == null){
            return true;
        }
        for (int adj:adjacent) {
            if (visited.contains(adj))
                continue;
            if (!isValidate(adj,target,visited))
                return false;
        }
        return true;
    }

    @Override
    protected int getRandomCost(int i, int j) {
        return 0;
    }
}

