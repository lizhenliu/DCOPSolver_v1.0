package com.edu.cqu.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SyncMailer extends Process {

    private Map<Integer,SyncAgent> agents;
    private Queue<Message> messageQueue;
    private FinishedListener listener;

    public SyncMailer() {
        super("syncMailer");
        agents = new HashMap<>();
        messageQueue = new LinkedList<>();
    }

    public SyncMailer(FinishedListener finishedlistener) {
        this();
        this.listener = finishedlistener;
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

    public void register(SyncAgent syncAgent) {
        agents.put(syncAgent.id,syncAgent);
    }
}
