package edu.cqu.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AsyncMailer extends Process {

    private Map<Integer,AsyncAgent> agents;
    private Queue<Message> messagesQueue;
    private FinishedListener listener;

    public AsyncMailer() {
        super("asyncMailer");
        messagesQueue = new LinkedList<>();
        agents = new HashMap<>();
    }

    public AsyncMailer(FinishedListener finishiedlistener) {
        this();
        listener = finishiedlistener;
    }

    @Override
    public void postExecution() {

    }

    @Override
    public void execution() {

    }

    @Override
    public void preExecution() {

    }

    public void registerAgent(AsyncAgent asyncAgent) {
        agents.put(asyncAgent.id,asyncAgent);
    }
}
