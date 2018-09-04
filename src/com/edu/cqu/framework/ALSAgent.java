package com.edu.cqu.framework;

import com.edu.cqu.core.Message;
import com.edu.cqu.core.SyncMailer;
import com.edu.cqu.ordering.BFSSyncAgent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class ALSAgent extends BFSSyncAgent {

    public static final int MSG_TYPE_ACCCOST = 0XFFFBD0;
    public static final int MSG_TYPE_BESTCOST = 0XFFFBD1;
    public final static String KEY_BESTCOST="KEY_BESTCOST";
    public final static String KEY_BESTCOSTINCYCLE="KEY_BESTCOSTINCYCLE";

    private LinkedList<Integer> valueIndexList;
    private LinkedList<Integer> localCostList;
    private Map<Integer,LinkedList<Integer>> childrenMsgView;

    private int accCost;
    protected int bestCost;
    private boolean isChanged;
    protected int bestValue;
    protected double[] bestCostInCycle;
    private int alsCycleCount;

    public ALSAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
        valueIndexList = new LinkedList<>();
        localCostList = new LinkedList<>();
        childrenMsgView = new HashMap<>();
        bestCostInCycle = new double[9999];

        accCost = 0;
        alsCycleCount = 0;
        bestCost = Integer.MAX_VALUE;
        isChanged = false;
        bestValue = -1;
    }

    protected void alsWork(){
        valueIndexList.add(valueIndex);
        if (!isLeafAgent()){
            localCostList.add(localCost);
        }else {
            accCost = localCost;
            sendAccCostMessage(accCost);
        }
    }

    private void sendAccCostMessage(int cost) {
        Message msg = new Message(this.id,this.parent,MSG_TYPE_ACCCOST,cost);
        sendMessage(msg);
    }

    protected void disposeAccCostMessage(Message msg) {
        if (childrenMsgView.containsKey(msg.getIdSender())){
            childrenMsgView.get(msg.getIdSender()).add((int)msg.getValue());
        }else {
            LinkedList<Integer> tempList = new LinkedList<>();
            tempList.add((int)msg.getValue());
            childrenMsgView.put(msg.getIdSender(),tempList);
        }

        boolean isEnough = true;
        for (int childrenId : children){
            if (!childrenMsgView.containsKey(childrenId)){
                isEnough = false;
                break;
            }
        }

        if (isEnough){
            accCost = localCostList.removeFirst();
            for (int  childrenId : children) {
                LinkedList<Integer> tempList = childrenMsgView.remove(childrenId);
                accCost += tempList.removeFirst();
                if (!tempList.isEmpty()){
                    childrenMsgView.put(childrenId,tempList);
                }
            }
            isChanged = false;
            if (!isRootAgent()){
                sendAccCostMessage(accCost);
            }else {
                alsCycleCount ++;
                accCost = accCost/2;
                if (accCost < bestCost){
                    bestValue = valueIndexList.removeFirst();
                    isChanged = true;
                    bestCost = accCost;
                }else {
                    valueIndexList.removeFirst();
                    isChanged = false;
                }

                sendBestCostMessage(isChanged);
                bestCostInCycle[alsCycleCount] = bestCost;
                if (bestCostInCycle.length == alsCycleCount){
                    double[] temp = new double [bestCostInCycle.length * 2];
                    for (int i = 0; i <= bestCostInCycle.length; i++) {
                        temp[i] = bestCostInCycle[i];
                    }
                    bestCostInCycle = temp;
                }

            }
        }
    }

    protected void alsStopRunning(){
        if (valueIndexList.isEmpty()){
            if (isRootAgent()){
                double [] temp = new double [alsCycleCount];
                for (int i = 0; i < alsCycleCount; i++) {
                    temp[i] = bestCostInCycle[i];
                }
                bestCostInCycle = temp;
            }
            valueIndex = bestValue;
            runFinished();
        }
    }

    protected void sendBestCostMessage(boolean isChanged) {
        for (int childrenId : children) {
            Message msg = new Message(this.id,childrenId,MSG_TYPE_BESTCOST,isChanged);
            sendMessage(msg);
        }
    }

    protected void disposeBestMessage(Message msg){
        if ((boolean)msg.getValue()){
            bestValue = valueIndexList.removeFirst();
            isChanged = true;
        }else {
            valueIndexList.removeFirst();
            isChanged = false;
        }
        if (!isLeafAgent()){
            sendBestCostMessage(isChanged);
        }
    }

    public double[] getBestCostInCycle() {
        return bestCostInCycle;
    }
}
