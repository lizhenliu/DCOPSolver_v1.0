package com.edu.cqu.core;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class SyncAgent extends Agent {

    private Queue<Message> messageQueue;
    protected SyncMailer syncMailer;
    private int pendingValueIndex;

    public SyncAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains);
        this.messageQueue = messageQueue;
        this.syncMailer = syncMailer;
        messageQueue = new LinkedList<>();
        syncMailer.register(this);
        pendingValueIndex = -1;
    }

    public void assignmentValueIndex(int pendingValueIndex){
        this.pendingValueIndex = pendingValueIndex;
    }

    @Override
    protected void postInit() {
        super.postInit();
    }

    @Override
    public void postExecution() {

    }

    @Override
    protected void runFinished() {

    }



    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void disposeMessage(Message message) {

    }

    @Override
    public void execution() {

    }

    @Override
    public void preExecution() {

    }

    @Override
    protected void initRun() {

    }

    public void addMessage(Message message){
        messageQueue.add(message);
    };
}
