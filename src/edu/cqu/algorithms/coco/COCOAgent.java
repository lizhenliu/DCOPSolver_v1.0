package edu.cqu.algorithms.coco;

import edu.cqu.core.*;
import edu.cqu.result.ResultCycle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class COCOAgent extends AsyncAgent {

    public static final int MSG_TYPE_INQMSG = 101;
    public static final int MSG_TYPE_COSTMSG = 102;
    public static final int MSG_TYPE_SETVAL = 103;
    public static final int MSG_TYPE_UPDSTATE = 104;

    public static final int AGENT_TYPE_IDLE = 201;
    public static final int AGENT_TYPE_ACTIVE = 202;
    public static final int AGENT_TYPE_HOLD = 203;
    public static final int AGENT_TYPE_DONE = 204;

    private int agentType;
    private HashMap<Integer,Integer> neighborView;
    private HashMap<Integer,Integer> sitaView;
    private HashMap<Integer,Integer> neighborStateView;
    private HashMap<Integer,HashMap<Integer,Integer>> neighborsCostView;
    private HashSet<Integer> deltaSet;
    private int belta;
    private int cycleTag;
    
    private int idleNeibors;
    private int activeNeibors;
    private int costMsgCount;

    public COCOAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, AsyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
        neighborView = new HashMap<>();
        sitaView = new HashMap<>();
        neighborStateView = new HashMap<>();
        neighborsCostView = new HashMap<>();
        deltaSet = new HashSet<>();
    }

    @Override
    protected void initRun() {
        agentType = AGENT_TYPE_IDLE;
        belta = 1;
        costMsgCount = 0;
        for (int neighborId:neighbors) {
            neighborView.put(neighborId,-1);
            neighborStateView.put(neighborId,AGENT_TYPE_IDLE);
        }
        if (this.id == 1){
            startCoCoA();
        }
    }

    private void startCoCoA() {
        System.out.println("test");
        if (agentType == AGENT_TYPE_IDLE || agentType == AGENT_TYPE_HOLD){
            System.out.println("testA");
            agentType = AGENT_TYPE_ACTIVE;
            sendUPDMessage(agentType);
            sendINQMessage(this.neighborView);
        }
    }

    private void sendUPDMessage(int agentType) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_UPDSTATE,agentType);
            sendMessage(message);
        }
    }

    private void sendINQMessage(HashMap<Integer,Integer> view) {
        for (int neighborId : neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_INQMSG,view);
            sendMessage(message);
        }
    }

    @Override
    protected void runFinished() {
        ResultCycle resultCycle = new ResultCycle();
        resultCycle.setAgentValues(id,valueIndex);
        asyncMailer.setResult(id,resultCycle);
    }

    @Override
    public void disposeMessage(Message message) {
        switch (message.getMsgType()){
            case MSG_TYPE_COSTMSG:
                disposeCostMessage(message);
                break;
            case MSG_TYPE_INQMSG:
                disposeINQMessage(message);
                break;
            case MSG_TYPE_SETVAL:
                disposeSetValMessage(message);
                break;
            case MSG_TYPE_UPDSTATE:
                disposeUPDMessage(message);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeCostMessage(Message message) {
        neighborsCostView.put(message.getIdSender(), (HashMap<Integer, Integer>) message.getValue());
        costMsgCount++;
        if (costMsgCount == neighbors.length){
            System.out.println("ddd");
            decision();
        }
    }

    private void decision() {
        int cost = Integer.MAX_VALUE;
        for (int i = 0; i < domain.length; i++) {
            int tempCost = 0;
            for (int neighborId:neighborsCostView.keySet()) {
                tempCost += neighborsCostView.get(neighborId).get(i);
            }
            if (tempCost < cost){
                cost = tempCost;
                deltaSet.clear();
                deltaSet.add(i);
            }else if (tempCost == cost){
                deltaSet.add(i);
            }
        }
        HashSet<Integer> tempSet = deltaSet;
        Object[] tempArray = tempSet.toArray();
        int q = (int) tempArray[(int) (Math.random()*tempSet.size())];
        valueIndex = domain[q];
        System.out.println("valueIndex: " + valueIndex);
        agentType = AGENT_TYPE_DONE;
        sendUPDMessage(agentType);
        sendSetValMessage(this.neighborView);
    }

    private void sendSetValMessage(HashMap<Integer,Integer> view) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_SETVAL,view);
            sendMessage(message);
        }
    }

    private void disposeINQMessage(Message message) {
        HashMap<Integer,Integer> view = new HashMap<>();
        this.neighborView = mergeLocalView(view);
        sitaView = calSitaView(message.getIdSender());
        sendCostMessage(sitaView);
    }

    private void sendCostMessage(HashMap<Integer,Integer> view) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_COSTMSG,view);
            sendMessage(message);
        }
    }

    private HashMap<Integer,Integer> mergeLocalView(HashMap<Integer,Integer> view) {
        HashMap<Integer,Integer> newView = this.neighborView;
        for (int neighborId:view.keySet()) {
            newView.put(neighborId,view.get(neighborId));
        }
        return newView;
    }

    private HashMap<Integer,Integer> calSitaView(int neiIndex) {
        HashMap<Integer,Integer> view = new HashMap<>();
        int cost = Integer.MAX_VALUE;
        for (int j = 0; j < neighborDomains.get(neiIndex).length; j++) {
            for (int i = 0; i < domain.length; i++) {
                cost = Integer.MAX_VALUE;
                int tempcost = 0;
                for (int neighborId:neighbors) {
                    if (neighborId == neiIndex){
                        tempcost += constraintCosts.get(neighborId)[i][j];
                    } else {
                        if (this.neighborView.get(neighborId)==-1){
                            tempcost += meanConstraint(i,neighborId);
                        } else {
                            tempcost += constraintCosts.get(neighborId)[i][this.neighborView.get(neighborId)];
                        }
                    }
                }
                if(tempcost < cost){
                    cost = tempcost;
                }
            }
            view.put(j,cost);
        }
        return view;
    }

    private int meanConstraint(int value,int neighborId) {
       int cost = 0;
        for (int i = 0; i < neighborDomains.get(neighborId).length; i++) {
            cost += constraintCosts.get(neighborId)[value][i];
        }
        cost /= neighborDomains.get(neighborId).length;
        return  cost;
    }


    @SuppressWarnings("unchecked")
    private void disposeSetValMessage(Message message) {
        HashMap<Integer,Integer> reView = (HashMap<Integer, Integer>) message.getValue();
        this.neighborView = mergeLocalView(reView);
    }

    private void disposeUPDMessage(Message message) {
        System.out.println("update");
        int reState = (int) message.getValue();
        neighborStateView.put(message.getIdSender(), reState);
        updateNeighborState();
        if (agentType == AGENT_TYPE_HOLD
                && (activeNeibors == 0 || idleNeibors == 0)){
            belta ++;
            startCoCoA();
        }else if (reState == AGENT_TYPE_DONE && agentType == AGENT_TYPE_HOLD){
            startCoCoA();
        }else {
            this.agentType = AGENT_TYPE_DONE;
            sendUPDMessage(agentType);
            System.out.println("finish");
            stopProcess();
        }
    }

    private void updateNeighborState() {
        idleNeibors = 0;
        activeNeibors = 0;
        for (int neighborId:neighbors) {
            if (neighborStateView.get(neighborId)==AGENT_TYPE_IDLE){
                idleNeibors ++;
            }else if (neighborStateView.get(neighborId)==AGENT_TYPE_ACTIVE){
                activeNeibors ++;
            }
        }
    }

}
