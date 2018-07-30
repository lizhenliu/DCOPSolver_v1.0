package com.edu.cqu.core;


import java.util.HashMap;
import java.util.Map;

public abstract class Agent extends Process {

    protected int id;
    protected int valueIndex;
    protected int[] domains;
    protected int[] neighbors;
    protected int ncccs;

    protected Map<Integer,Integer> localView;
    protected Map<Integer,int[]> neighborDomains;
    protected Map<Integer,int[][]> constraintCosts;

    public Agent(String threadName, int id, int valueIndex,int[] neighbors, int[] domains, int ncccs, Map<Integer, int[]> neighborDomains, Map<Integer, int[][]> constraintCosts) {
        super("Agent " + id);
        this.id = id;
        this.valueIndex = valueIndex;
        this.domains = domains;
        this.neighbors = neighbors;
        this.ncccs = ncccs;
        this.neighborDomains = neighborDomains;
        this.constraintCosts = constraintCosts;
        localView = new HashMap<>();
    }

    @Override
    protected void postExecution() {
        runFinished();
    }

    protected abstract void runFinished();

    @Override
    protected void preExecution() {
        for (int neighborId:neighbors) {
            localView.put(neighborId,0);
        }
        initRun();
        postInit();
    }

    protected abstract void postInit();
    protected abstract void initRun();

    protected int getLocalCosts(){
        int cost = 0;
        for (int neighborId:neighbors) {
            cost += constraintCosts.get(neighborId)[valueIndex][localView.get(neighborId)];
        }
        return cost;
    }

    protected abstract void disposeMessage(Messsage messsage);
    protected abstract void sendMessage(Messsage messsage);

}
