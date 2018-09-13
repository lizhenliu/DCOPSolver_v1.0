package edu.cqu.core;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Process extends Thread {
    private Thread thread;
    private String threadName;
    private AtomicBoolean isRunning;
    private boolean suppressOutput;

    public Process(String threadName) {
        this.threadName = threadName;
        isRunning = new AtomicBoolean(false);
        suppressOutput = false;
    }

    public void startProcess(){
        synchronized (isRunning){
            if (isRunning.get()){
                return;
            }
            isRunning.set(true);
        }
        Thread thread = new Thread(new Runnable() {
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
                if (!suppressOutput){
                    System.out.println(threadName + " is stopped");
                }
            }
        },threadName);
        thread.start();
    }

    public void stopProcess(){
        synchronized (isRunning){
            if (isRunning.get()){
                isRunning.set(false);
            }
        }
    }

    public abstract void postExecution();

    public abstract void execution();

    public abstract void preExecution();

    public boolean isRunning(){
        return isRunning.get();
    }

    public void setSuppressOutput(boolean suppressOutput) {
        this.suppressOutput = suppressOutput;
    }
}
