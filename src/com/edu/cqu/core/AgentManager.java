package com.edu.cqu.core;

import com.edu.cqu.parser.AgentsParser;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AgentManager {
    private static final String METHOD_ASYNC = "ASYNC";
    private static final String METHOD_SYNC = "SYNC";

    private List<Agent> agents;
    private AsyncMailer asyncMailer;
    private SyncMailer syncMailer;
    private static Map<String,AgentDescriptor> agentDescriptors;
    static {
        agentDescriptors = new AgentsParser("agent.xml").paraser();
    }

    public AgentManager(String agentType,Problem problem,FinishedListener listener) {
        agents = new LinkedList<>();
        AgentDescriptor descriptor = agentDescriptors.get(agentType.toUpperCase());
        if (descriptor.method.equals(METHOD_ASYNC)){
            asyncMailer = new AsyncMailer(listener);
        }else {
            syncMailer = new SyncMailer(listener);
        }

        for (int id:problem.allId) {
            Agent agent = null;
            try {
                Class clazz = Class.forName(descriptor.className);
                Constructor constructor = clazz.getConstructors()[0];
                agent = (Agent) constructor.newInstance(id,problem.domains.get(id),problem.neighbors.get(id),problem.constraintsCost.get(id),problem.getNeighborDomain(id));
            }catch (Exception e){
                throw new RuntimeException("init exception");
            }
        }
    }

    public void startAgents(){
        for (Agent agent:agents) {
            agent.startProcess();
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (asyncMailer != null){
            asyncMailer.startProcess();
        }
        else if(syncMailer != null){
            syncMailer.startProcess();
        }
    }
}
