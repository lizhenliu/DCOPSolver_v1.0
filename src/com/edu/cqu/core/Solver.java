package com.edu.cqu.core;

import com.edu.cqu.parser.ProblemParser;

import java.util.concurrent.atomic.AtomicBoolean;

public class Solver {
    private AtomicBoolean isSolving;
    private  Thread threadMonitor;
    public Solver() {
        isSolving = new AtomicBoolean(false);
    }

    public void solve(String agentDescriptorPath, String agentType, String problemPath, FinishedListener listener, boolean showPesudoTreeGraph){
        ProblemParser parser = new ProblemParser(problemPath);
        Problem problem = parser.parse();
        AgentManager manager = new AgentManager(agentDescriptorPath,agentType,problem,listener,false);
    }
}
