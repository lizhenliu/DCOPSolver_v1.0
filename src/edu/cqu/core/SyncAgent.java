package edu.cqu.core;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class SyncAgent extends Agent {

    private Queue<Message> messageQueue;
    protected SyncMailer mailer;
    private int pendingValueIndex;

    public SyncAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains);
        this.mailer = mailer;
        messageQueue = new LinkedList<>();
        mailer.register(this);
        pendingValueIndex = -1;
    }

    public void assignmentValueIndex(int pendingValueIndex){
        this.pendingValueIndex = pendingValueIndex;
    }

    @Override
    protected void postInit() {
        super.postInit();
        mailer.agentDone(this.id);
    }

    @Override
    public void sendMessage(Message message){
        mailer.addMessage(message);
    }

    @Override
    public void execution() {
        if (mailer.getPhase() == SyncMailer.PHASE_AGENT && !mailer.isDone(this.id)){
            while (!messageQueue.isEmpty()){
                disposeMessage(messageQueue.poll());
            }
            allMessageDisposed();
            mailer.agentDone(this.id);
            if (pendingValueIndex >= 0){
                valueIndex = pendingValueIndex;
                pendingValueIndex = -1;
            }
        }
    }

    protected void allMessageDisposed(){};

    public void addMessage(Message message){
        messageQueue.add(message);
    };
}
