package com.edu.cqu.core;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Process {
    private Thread thread;
    private String threadName;
    private AtomicBoolean isRunning;

    public Process(String threadName) {
        this.threadName = threadName;
        isRunning = new AtomicBoolean(false);
    }

    public void startProcess(){
        synchronized (isRunning){
            if (isRunning.get()){
                return;
            }
            isRunning.set(true);
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                preExecution();
                while (true){
                    synchronized (isRunning){
                        if (!isRunning.get()){
                            break;
                        }
                    }
                    execution();
                }
                postExecution();
            }
        },this.threadName);
        thread.start();
    }

    public void stopProcess(){
        synchronized (isRunning){
            if (isRunning.get()){
                isRunning.set(false);
            }
        }
    }


    protected abstract void postExecution();

    protected abstract void execution();

    protected abstract void preExecution();

}
