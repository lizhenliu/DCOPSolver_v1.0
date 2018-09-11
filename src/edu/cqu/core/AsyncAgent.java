package edu.cqu.core;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class AsyncAgent extends Agent {

    private Queue<Message> messageQueue;
    protected AsyncMailer asyncMailer;

    public AsyncAgent(int id, int[] domain, int[] neighbors, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighborDomains, AsyncMailer asyncMailer) {
        super(id, domain, neighbors, constraintCosts, neighborDomains);
        this.asyncMailer = asyncMailer;
        messageQueue = new LinkedList<>();
        asyncMailer.registerAgent(this);
    }


    @Override
    protected void initRun() {

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

    public void addMessage(Message message) {
        synchronized (messageQueue){
            messageQueue.add(message);
        }
    }
}
