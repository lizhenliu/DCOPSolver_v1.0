package edu.cqu.algorithms.mgm;

import edu.cqu.core.Message;
import edu.cqu.core.SyncAgent;
import edu.cqu.core.SyncMailer;

import java.util.Map;

public class MGM2Agent extends SyncAgent {
    public static final int MSG_TYPE_VALUE = 101;
    public static final int MSG_TYPE_OFFER = 102;
    public static final int MSG_TYPE_GAIN = 103;
    public static final int MSG_TYPE_ACCEPT = 104;
    public static final int MSG_TYPE_GO = 105;

    public static final int CYCLE_VALUE = 201;
    public static final int CYCLE_OFFER = 202;
    public static final int CYCLE_GAIN = 203;
    public static final int CYCLE_ACCEPT = 204;
    public static final int CYCLE_GO = 205;

    public static final int CYCLE_COUNT = 20;
    private int cycleCount;

    public MGM2Agent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
        valueIndex = (int) (Math.random()*domain.length);
        
    }

    @Override
    protected void initRun() {

    }

    @Override
    protected void runFinished() {

    }

    @Override
    public void disposeMessage(Message message) {

    }
}
