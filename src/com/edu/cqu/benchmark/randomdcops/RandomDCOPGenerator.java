package com.edu.cqu.benchmark.randomdcops;

import com.edu.cqu.benchmark.AbstractGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RandomDCOPGenerator extends AbstractGraph {

    private Map<Integer,Set<Integer>> adjacenTable;
    private int density;

    public RandomDCOPGenerator(String name, int nbAgents, int domainsize, int minCost, int maxCost, int density) {
        super(name, nbAgents, domainsize, minCost, maxCost);
        this.density = density;
        adjacenTable = new HashMap<>();
    }


    public void generateInitConnectedGraph(){
        for (int i = 0; i < nbAgents-1; i++) {
            int startPoint;
            int endPoint;
            while(true){
                startPoint = random.nextInt(nbAgents)+1;
                endPoint = random.nextInt(nbAgents)+1;
                Set<Integer> visitedSet = new HashSet<>();
                if (isValidate(endPoint,startPoint,visitedSet)){
                    break;
                }
            }
            source.add(Integer.min(startPoint,endPoint));
            dest.add(Integer.max(startPoint,endPoint));
            nbConstraints++;
            nbRelations++;
            Set<Integer> adjacent = adjacenTable.get(startPoint);
            if (adjacent.isEmpty()){
                adjacent = new HashSet<>();
                adjacenTable.put(startPoint,adjacent);
            }
            adjacent.add(endPoint);
            adjacent = adjacenTable.get(endPoint);
            if (adjacent.isEmpty()){
                adjacent = new HashSet<>();
                adjacenTable.put(endPoint,adjacent);
            }
            adjacent.add(startPoint);
        }
    }

    private boolean isValidate(int nextPoint, int target, Set<Integer> visitedSet) {
        if (nextPoint == target){
            return false;
        }
        visitedSet.add(nextPoint);
        Set<Integer> adjacent = adjacenTable.get(nextPoint);
        for (int adj:adjacent) {
            if (visitedSet.contains(adj)){
                continue;
            }
            if (!isValidate(adj,target,visitedSet)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void generateConstraint() {
        generateInitConnectedGraph();
        int maxEdges = (int)(nbAgents * (nbAgents - 1) * density / 2);
        for (int i = nbAgents - 1; i < maxEdges; i++) {
            int startPoint = random.nextInt(nbAgents)+1;
            int endPoint = random.nextInt(nbAgents)+1;
            while (startPoint == endPoint){
                startPoint = random.nextInt(nbAgents)+1;
                endPoint = random.nextInt(nbAgents)+1;
            }
            adjacenTable.get(startPoint).add(endPoint);
            adjacenTable.get(endPoint).add(startPoint);
            source.add(Integer.min(startPoint,endPoint));
            dest.add(Integer.max(startPoint,endPoint));
            nbConstraints++;
            nbRelations++;
        }
    }
}
