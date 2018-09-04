package com.edu.cqu.algorithms.dsa;

import com.edu.cqu.core.Message;
import com.edu.cqu.core.SyncAgent;
import com.edu.cqu.core.SyncMailer;
import java.util.Map;

public class DSAAgent extends SyncAgent {

    public static final int MSG_TYPE_VALUE = 101;
    public static final int MSG_TYPE_COST = 102;

    public DSAAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
    }

    @Override
    protected void initRun() {
        super.initRun();
        valueIndex = (int) (Math.random()*domain.length);
        localCost  = Integer.MAX_VALUE;
        sendValueMessage(valueIndex);
    }

    private void sendValueMessage(int value) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_VALUE,value);
            sendMessage(message);
        }
    }


}
