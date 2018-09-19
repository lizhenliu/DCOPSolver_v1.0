package edu.cqu.algorithms.mgm;

import edu.cqu.core.Message;
import edu.cqu.core.SyncAgent;
import edu.cqu.core.SyncMailer;
import edu.cqu.result.ResultCycle;

import java.util.HashMap;
import java.util.Map;

public class MGMAgent extends SyncAgent {

    public static final int MSG_TYPE_VALUE = 101;
    public static final int MSG_TYPE_GAIN = 102;
    public static final int CYCLE_COUNT = 20;
    private int cycleCount;
    private HashMap<Integer,Integer> gainView;
    private int valueCount;
    private int gainCount;
    private int suggestValue;
    private int gain;

    public MGMAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
        gainView = new HashMap<>();
    }

    @Override
    protected void initRun() {
        valueIndex = (int) (Math.random()*domain.length);
        localCost = Integer.MAX_VALUE;
        cycleCount = 0;
        gainCount = 0;
        valueCount = 0;
        gain = 0;
        suggestValue = valueIndex;
        sendValueMessage(valueIndex);
    }

    private void sendValueMessage(int value) {
        for (int neighborId : neighbors) {
             Message message  = new Message(this.id,neighborId,MSG_TYPE_VALUE,value);
             sendMessage(message);
        }
    }

    @Override
    public void disposeMessage(Message message) {
        switch (message.getMsgType()){
            case MSG_TYPE_VALUE:
                disposeValueMessage(message);
                break;
            case MSG_TYPE_GAIN:
                disposeGainMessage(message);
                break;
        }
    }

    private void disposeGainMessage(Message message) {
        gainCount++;
        gainView.put(message.getIdSender(), (Integer) message.getValue());
        if (gainCount == neighbors.length){
            gainCount = 0;
            decision();
        }
    }

    private void decision() {
        if (isMax(gainView)){
            valueIndex = suggestValue;
        }
        sendValueMessage(valueIndex);
    }

    private boolean isMax(HashMap<Integer,Integer> gainView) {
        boolean isMax = true;
        for (int neighborId:neighbors) {
            if (gain < gainView.get(neighborId)){
                isMax = false;
                break;
            }
        }
        return isMax;
    }

    private void disposeValueMessage(Message message) {
        valueCount++;
        localView.put(message.getIdSender(), (Integer) message.getValue());
        if (valueCount == neighbors.length){
            if (cycleCount < CYCLE_COUNT){
                cycleCount ++;
                valueCount = 0;
                calGain();
            }else {
                stopProcess();
            }
        }
    }

    private void calGain() {
        gain = 0;
        localCost = getLocalCost();
        int minCost = localCost;
        suggestValue = valueIndex;
        for (int i = 0; i < domain.length; i++) {
            int tempCost = calLocalCost(i);
            if (tempCost < minCost){
                minCost = tempCost;
                suggestValue = i;
            }
        }
        gain = localCost - minCost;
        sendGainMessage(gain);
    }

    private void sendGainMessage(int gain) {
        for (int neighborId : neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_GAIN,gain);
            sendMessage(message);
        }
    }

    @Override
    protected void runFinished() {
        ResultCycle resultCycle = new ResultCycle();
        resultCycle.setAgentValues(id,valueIndex);
        mailer.setResultCycle(id,resultCycle);
    }
}
