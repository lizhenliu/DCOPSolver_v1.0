package edu.cqu.algorithms.coco;

import edu.cqu.core.Message;
import edu.cqu.core.SyncAgent;
import edu.cqu.core.SyncMailer;

import java.net.Inet4Address;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class COCOAgent extends SyncAgent {

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
    private HashSet<Integer> deltaSet;
    private int belta;

    private int uniNum;
    private int idleNeibors;
    private int activeNeibors;

    public COCOAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
        neighborView = new HashMap<>();
        sitaView = new HashMap<>();
        deltaSet = new HashSet<>();
        neighborStateView = new HashMap<>();
    }

    @Override
    protected void initRun() {
        this.agentType = AGENT_TYPE_IDLE;
        belta = 1;
        uniNum = domain.length; //?
        idleNeibors = neighbors.length;
        activeNeibors = 0;
        for (int neighborId:neighbors) {
            localView.put(neighborId,-1);
        }
        if (this.id == 1){
            if (this.agentType == AGENT_TYPE_IDLE || this.agentType == AGENT_TYPE_HOLD){
                this.agentType = AGENT_TYPE_ACTIVE;
                sendUPDStateMessage(this.id,agentType);
                sendINQMSGMessage(this.id,this.neighborView);
                //waiting for all
                uniNum = calUninum(sitaView);
                if (uniNum <= belta || (activeNeibors == 0 || idleNeibors == 0)){
                    int q = (int) (Math.random()*deltaSet.size());
                    valueIndex = domain[q];
                    agentType = AGENT_TYPE_DONE;
                    sendUPDStateMessage(this.id,agentType);
                    sendSetValMessage(localView);
                }else {
                    agentType = AGENT_TYPE_HOLD;
                    sendUPDStateMessage(this.id,agentType);
                }
            }
        }
    }

    private void disposeUPDStateMessage(Message message) {
        int neighborState = (int) message.getValue();
        neighborStateView.put(message.getIdSender(), neighborState);
        if ((neighborState == AGENT_TYPE_HOLD)&&(this.agentType == AGENT_TYPE_HOLD)&&(idleNeibors == 0||activeNeibors == 0)){
            belta ++;
            //repeat algorithm
        }else if ((neighborState == AGENT_TYPE_DONE)&&this.agentType == AGENT_TYPE_HOLD){
            //repeat allgorithm
        }

    }


    private void sendSetValMessage(HashMap<Integer,Integer> view) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_SETVAL,view);
            sendMessage(message);
        }
    }

    private int calUninum(HashMap<Integer,Integer> view) {
        int count = 0;
        int minCost = Integer.MAX_VALUE;
        for (int id:view.keySet()) {
            if (view.get(id) < minCost){
                HashSet<Integer> tempSet = new HashSet<>();
                tempSet.add(id);
                deltaSet = tempSet;
                minCost = view.get(id);
                count = 1;
            }else if (view.get(id) == minCost){
                deltaSet.add(id);
                count++;
            }
        }
        return count;
    }

    private void calcuSitaView(int neighborId) {
        for (int k = 0; k < neighborDomains.get(neighborId).length; k++) {
            int tempCost = 0;
            for (int i = 0; i < domain.length; i++) {
                int cost = 0;
                int minCost = Integer.MAX_VALUE;
                for (int neighborIndex : neighbors) {
                    if (neighborIndex == neighborId){
                        cost += constraintCosts.get(neighborId)[i][k];
                    }else {
                        if (localView.get(neighborId) == -1){
                            cost += meanCost(neighborIndex,i);
                        }else {
                            cost += constraintCosts.get(neighborIndex)[i][localView.get(neighborIndex)];
                        }
                    }
                    if (cost < minCost){
                        minCost = cost;
                    }
                }
                tempCost = minCost;
            }
            sitaView.put(k,tempCost);
        }
    }


    private int meanCost(int neighborId, int value) {
        int cost = 0;
        int constraint = 0;
        for (int i = 0; i < neighborDomains.get(neighborId).length; i++) {
            cost += constraintCosts.get(neighborId)[value][i];
        }
        constraint = (int)(cost / neighborDomains.get(neighborId).length);
        return constraint;
    }

    private void sendINQMSGMessage(int index, HashMap<Integer,Integer> view) {
        for (int neighborId : neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_UPDSTATE,view);
            sendMessage(message);
        }
    }

    private void sendUPDStateMessage(int index, int type) {
        for (int neighborId : neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_UPDSTATE,type);
            sendMessage(message);
        }
    }

    @Override
    protected void runFinished() {

    }

    @Override
    public void disposeMessage(Message message) {
        switch (message.getMsgType()){
            case MSG_TYPE_COSTMSG:
                disposeCostMessage(message);
                break;
            case MSG_TYPE_UPDSTATE:
                disposeUPDStateMessage(message);
                break;
            case MSG_TYPE_INQMSG:
                disposeINQMessage(message);
                break;
            case MSG_TYPE_SETVAL:
                disposeSetValMessage(message);
                break;
        }
    }

    private void disposeCostMessage(Message message) {

    }

    private void disposeINQMessage(Message message) {
        calcuSitaView(message.getIdSender());
        sendCostMessage(sitaView);
    }

    private void sendCostMessage(HashMap<Integer,Integer> view) {
        for (int neighborId:neighbors) {
            Message message = new Message(this.id,neighborId,MSG_TYPE_COSTMSG,view);
            sendMessage(message);
        }
    }

    private void disposeSetValMessage(Message message) {

    }
}
