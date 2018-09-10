package edu.cqu.algorithms.dsa;

import edu.cqu.core.Message;
import edu.cqu.core.SyncAgent;
import edu.cqu.core.SyncMailer;
import java.util.Map;

public class DSAAgent extends SyncAgent {

    public static final int MSG_TYPE_VALUE = 101;
    public static final int MSG_TYPE_COST = 102;
    public static final double PI = 0.4;
    private int valueCount;

    public DSAAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
    }

    @Override
    protected void initRun() {
        super.initRun();
        valueIndex = (int) (Math.random()*domain.length);
        localCost  = Integer.MAX_VALUE;
        valueCount = 0;
        sendValueMessage(valueIndex);
    }

    private void sendValueMessage(int value) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_VALUE,value);
            sendMessage(message);
        }
    }

    @Override
    public void disposeMessage(Message message) {
        super.disposeMessage(message);
        switch (message.getMsgType()){
            case MSG_TYPE_VALUE:
                disposeValueMessage(message);
                break;
        }
    }

    private void disposeValueMessage(Message message) {
        localView.put(message.getIdSender(), (Integer) message.getValue());
        valueCount++;
        if (valueCount == neighbors.length){
            valueCount = 0;
            localCost = getLocalCost();
            if (Math.random() > PI){
                valueIndex = decision();
            }
            sendValueMessage(valueIndex);
        }
    }

    private int decision() {
        int value = -1;
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

    private int calLocalCost(int value) {
        int sumCost = 0;
        for (int neighborId : neighbors) {
            sumCost += constraintCosts.get(neighborId)[value][localView.get(neighborId)];
        }
        return sumCost;
    }
}
