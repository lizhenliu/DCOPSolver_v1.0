package edu.cqu.algorithms.dsa;

import edu.cqu.core.Message;
import edu.cqu.core.SyncAgent;
import edu.cqu.core.SyncMailer;
import edu.cqu.result.ResultCycle;

import java.util.HashMap;
import java.util.Map;

public class DSAAgent extends SyncAgent {

    public static final int MSG_TYPE_VALUE = 101;
    public static final int CYCLE_COUNT = 10;
    public static final double PI = 0.4;
    private int valueCount;
    private int cycleCount;

    public DSAAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
    }

    @Override
    protected void initRun() {
        this.valueIndex = (int) (Math.random()*domain.length);
        localCost  = Integer.MAX_VALUE;
        valueCount = 0;
        cycleCount = 1;
        sendValueMessage(valueIndex);
    }

    @Override
    protected void runFinished() {
        ResultCycle resultCycle = new ResultCycle();
        resultCycle.setAgentValues(id,valueIndex);
        mailer.setResultCycle(id,resultCycle);
    }

    private void sendValueMessage(int value) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_VALUE,value);
            sendMessage(message);
        }
    }

    @Override
    public void disposeMessage(Message message) {
        if (cycleCount < CYCLE_COUNT){
            switch (message.getMsgType()){
                case MSG_TYPE_VALUE:
                    disposeValueMessage(message);
                    break;
            }
        }else {
            stopProcess();
        }
    }

    private void disposeValueMessage(Message message) {
        localView.put(message.getIdSender(), (Integer) message.getValue());
        valueCount++;
        if (valueCount == neighbors.length){
            cycleCount ++;
            valueCount = 0;
            localCost = getLocalCost();
            HashMap<Integer,Integer> test = new HashMap<>();
            test = localView;
            if (Math.random() < PI){
                valueIndex = decision();
            }
            sendValueMessage(valueIndex);
        }
    }

    private int decision() {
        int value = valueIndex;
        int maxDelta = 0;
        for (int i = 0; i < domain.length; i++) {
            int tempDelta = localCost - calLocalCost(i);
            if (tempDelta > maxDelta){
                maxDelta = tempDelta;
                value = i;
            }
        }
        return value;
    }

}
