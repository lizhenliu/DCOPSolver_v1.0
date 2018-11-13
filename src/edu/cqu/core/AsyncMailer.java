package edu.cqu.core;

import edu.cqu.result.Result;

import java.util.*;

public class AsyncMailer extends Process {

    private Map<Integer,AsyncAgent> agents;
    private Result result;
    private Queue<Message> messagesQueue;
    private int messsagesCount;
    private long startTime;
    private FinishedListener listener;

    public AsyncMailer() {
        super("mailer");
        messagesQueue = new LinkedList<>();
        agents = new HashMap<>();
    }

    public AsyncMailer(FinishedListener finishiedlistener) {
        this();
        listener = finishiedlistener;
    }

    public void reister(AsyncAgent agent){
        agents.put(agent.id,agent);
    }

    public void addMessage(Message message){
        synchronized (messagesQueue){
            messagesQueue.add(message);
        }
    }

    @Override
    public void postExecution() {

    }

    public synchronized void setResult(int id,Result result){
        if (this.result == null) {
            this.result = result;
        } else {
            this.result.add(result);
            this.result.setAgentValues(id, result.getAgentValue(id));
        }

    }

    @Override
    public void execution() {
        synchronized (messagesQueue){
            while (!messagesQueue.isEmpty()){
                Message message = messagesQueue.poll();
                if (agents.get(message.getIdReceiver()).isRunning()) {
                    messsagesCount++;
                    agents.get(message.getIdReceiver()).addMessage(message);
                }
            }
            boolean canTerminate = true;
            for (AsyncAgent asyncAgent : agents.values()){
                if (asyncAgent.isRunning()){
                    canTerminate = false;
                    break;
                }
            }
            if (canTerminate){
                result.setMessageQuantity(messsagesCount);
                result.setTotalTime(new Date().getTime() - startTime);
                if (listener != null){
                    listener.onFinished(result);
                }
                stopProcess();
            }

        }
    }

    @Override
    public void preExecution() {
        startTime = new Date().getTime();
    }

    public void registerAgent(AsyncAgent asyncAgent) {
        agents.put(asyncAgent.id,asyncAgent);
    }
}
