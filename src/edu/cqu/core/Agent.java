package edu.cqu.core;

import java.util.HashMap;
import java.util.Map;

public abstract class Agent extends Process {

    protected int id;
    protected int[] domain;
    protected int[] neighbors;
    protected Map<Integer,int[]> neighborDomains;
    protected Map<Integer,int[][]> constraintCosts;
    protected HashMap<Integer,Integer> localView;
    protected int valueIndex;
    protected int localCost;

    public int ncccs;

    public Agent(int id,int[]domain,int[]neighbors,Map<Integer,int[][]> constraintCosts,Map<Integer,int[]> neighborDomains) {
        super("Agent: " +id);
        this.id = id;
        this.domain = domain;
        this.neighbors = neighbors;
        this.neighborDomains = neighborDomains;
        this.constraintCosts = constraintCosts;
        localView = new HashMap<>();
    }

    @Override
    public void preExecution() {
        for (int neighborId:neighbors) {
            localView.put(neighborId,0);
        }
        initRun();
        postInit();
    }

    protected void postInit() {
    }

    protected abstract void initRun();

    @Override
    public void postExecution() {
        runFinished();
    }

    protected abstract void runFinished();

    public abstract void sendMessage(Message message);
    public abstract void disposeMessage(Message message);

    public int getLocalCost(){
        int sumCost = 0;
        for (int neighborId : neighbors) {
            sumCost += constraintCosts.get(neighborId)[valueIndex][localView.get(neighborId)];
        }
        return sumCost;
    }

}
