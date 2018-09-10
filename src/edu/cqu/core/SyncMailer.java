package edu.cqu.core;

import edu.cqu.framework.ALSAgent;
import edu.cqu.result.ResultCycle;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncMailer extends Process {

    public static final int PHASE_AGENT = 1;
    public static final int PHASE_MAILER = 2;

    private Map<Integer,SyncAgent> agents;
    private Queue<Message> messageQueue;
    private int cycleCount;
    private int messageCount;
    private Set<Integer> agentReady;
    private AtomicInteger phase;
    private double[] costInCycle;
    private double[] bestCostInCycle;
    private int tail;
    private ResultCycle resultCycle;
    private long startTime;
    private FinishedListener listener;
    private Set<Agent> stoppedAgents;
    private boolean printCycle;
    private List<CycleListener> cycleListeners;
    private List<AgentIteratedOverListener> agentIteratedOverListeners;
    private double sumCost = 0;
    private int t;
    private int ncccs;
    private boolean recordCostInCycle;

    public SyncMailer() {
        super("syncMailer");
        agents = new HashMap<>();
        messageQueue = new LinkedList<>();
        agentReady = new HashSet<>();
        phase = new AtomicInteger(PHASE_AGENT);
        costInCycle = new double[2];
        bestCostInCycle = new double[2];
        stoppedAgents = new HashSet<>();
        cycleListeners = new LinkedList<>();
        agentIteratedOverListeners = new LinkedList<>();
        recordCostInCycle = true;
    }

    public SyncMailer(FinishedListener finishedlistener) {
        this();
        this.listener = finishedlistener;
    }

    public void setPrintCycle(boolean printCycle){
        this.printCycle = printCycle;
    }

    public void registerCycleListener(CycleListener listener){
        cycleListeners.add(listener);
    }



    @Override
    public void postExecution() {

    }

    @Override
    public void execution() {
        if (phase.get() == PHASE_MAILER){
            synchronized (messageQueue){
                while (!messageQueue.isEmpty()){
                    Message message = messageQueue.poll();
                    messageCount++;
                    if (agents.get(message.getIdReceiver()).isRunning()){
                        agents.get(message.getIdReceiver()).addMessage(message);
                        int senderNccc = agents.get(message.getIdSender()).ncccs;
                        int receiverNccc = agents.get(message.getIdReceiver()).ncccs;
                        ncccs = Integer.max(ncccs,senderNccc);
                        ncccs = Integer.max(ncccs,receiverNccc);
                        agents.get(message.getIdReceiver()).ncccs = Integer.max(senderNccc + t,receiverNccc);
                    }
                }
                boolean canTerminate = true;
                for (SyncAgent syncAgent : agents.values()) {
                    if (syncAgent.isRunning()){
                        canTerminate = false;
                    }else {
                        stoppedAgents.add(syncAgent);
                    }
                }
                if (recordCostInCycle){
                    if (tail == costInCycle.length - 1){
                        expand();
                    }
                    for (Agent agent : stoppedAgents) {
                        sumCost += agent.getLocalCost();
                    }
                    costInCycle[tail] = sumCost / 2;
                }
                sumCost = 0;
                if (recordCostInCycle){
                    Agent rootAgent = agents.get(1);
                    if (rootAgent instanceof ALSAgent){
                        bestCostInCycle[tail] = ((ALSAgent) rootAgent).getBestCostInCycle()[cycleCount];
                    }
                }
                tail++;
                for (CycleListener listener : cycleListeners) {
                    listener.onCycleChanged(tail);
                }
                for (AgentIteratedOverListener listener:agentIteratedOverListeners) {
                    listener.agentIteratedOver(agents);
                }
                if (printCycle){
                    System.out.println("cycle " + tail);
                }
                if (canTerminate){
                    stopProcess();
                }else {
                    cycleCount++;
                    agentReady.clear();
                    phase.set(PHASE_AGENT);
                }
            }
        }
    }

    public void addMessage(Message message) {
        synchronized (phase){
            while (phase.get() == PHASE_MAILER);
            synchronized (messageQueue){
                messageQueue.add(message);
            }
        }
    }

    private void expand() {
        double[] tmpCostInCycle = new double[costInCycle.length*2];
        double[] tmpBestCostInCycle = new double[bestCostInCycle.length*2];
        for (int i = 0; i < costInCycle.length; i++) {
            tmpCostInCycle[i] = costInCycle[i];
            tmpBestCostInCycle[i] = bestCostInCycle[i];
        }
        costInCycle = tmpCostInCycle;
        bestCostInCycle = tmpBestCostInCycle;
    }

    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public void preExecution() {
        startTime = new Date().getTime();
    }

    public void register(SyncAgent syncAgent) {
        agents.put(syncAgent.id,syncAgent);
    }

    public synchronized void agentDone(int id){
        synchronized (agentReady){
            agentReady.add(id);
            sumCost += agents.get(id).getLocalCost();
            if (agentReady.size() == agents.size() - stoppedAgents.size()){
                phase.set(PHASE_MAILER);
            }
        }
    }

    public synchronized boolean isDone(int id){
        return agentReady.contains(id);
    }

    public synchronized void setResultCycle(int id,ResultCycle resultCycle){
        if (this.resultCycle == null){
            this.resultCycle = resultCycle;
        }else {
            this.resultCycle.add(resultCycle);
            this.resultCycle.setAgentValue(id,resultCycle.getAgentValue(id));
        }
        if (this.resultCycle.getAgents().size() == agents.size()){
            this.resultCycle.setTotalTime(new Date().getTime() - startTime);
            this.resultCycle.setMessageQuantity(messageCount);
            if (recordCostInCycle){
                this.resultCycle.setCostInCycle(costInCycle,tail);
            }
            this.resultCycle.setNcccs(ncccs);
            if (listener != null){
                listener.onFinished(this.resultCycle);
            }
        }

    }

    public ResultCycle getResultCycle() {
        return resultCycle;
    }

    public synchronized int getPhase(){
        return phase.get();
    }

    public void registerAgentIteratedOverListener(AgentIteratedOverListener listener) {
        agentIteratedOverListeners.add(listener);
    }

    public interface CycleListener{
        void onCycleChanged(int cycle);
    }
}
