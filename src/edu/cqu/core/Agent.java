package edu.cqu.core;

import java.util.HashMap;
import java.util.Map;

public abstract class Agent extends Process {

    protected int id;
    protected int[] domain;
    protected int[] neighbors;
    protected Map<Integer,int[]> neighborDomains;
    protected Map<Integer,int[][]> constraintCosts;
    protected HashMap<Integer,Integer> neighborView;
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
        neighborView = new HashMap<>();
    }

    @Override
    public void preExecution() {
        for (int neighborId:neighbors) {
            neighborView.put(neighborId,0);
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
            sumCost += constraintCosts.get(neighborId)[valueIndex][neighborView.get(neighborId)];
        }
        return sumCost;
    }

    public int calLocalCost(int value){
        int sumCost = 0;
        for (int neighborId : neighbors) {
            sumCost += constraintCosts.get(neighborId)[value][neighborView.get(neighborId)];
        }
        return sumCost;
    }

    protected int updateLocalView(int neighbourId,int valueIndex){
        return neighborView.put(neighbourId,valueIndex);
    }

    protected int getNeighbourValue(int neighbourId){
        return neighborView.get(neighbourId);
    }

    public static String array2String(int[] arr){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < arr.length; i++){
            if (i != arr.length - 1){
                stringBuilder.append(arr[i] + ",");
            }
            else {
                stringBuilder.append(arr[i] + "]");
            }
        }
        return stringBuilder.toString();
    }

    public static String map2String(Map map){
        return map2String(map,null,null);
    }

    public static String map2String(Map map, Stringifier keyStringifier, Stringifier valueStringifier){
        StringBuilder stringBuilder = new StringBuilder();
        for (Object key : map.keySet()){
            String keyString = keyStringifier == null ? key.toString() : keyStringifier.stringify(key);
            String valueString = valueStringifier == null ? map.get(key).toString() : valueStringifier.stringify(map.get(key));
            stringBuilder.append(keyString + "=" + valueString + "\n");
        }
        return stringBuilder.toString();
    }


}
