package edu.cqu.ordering;

import edu.cqu.core.Message;
import edu.cqu.core.SyncAgent;
import edu.cqu.core.SyncMailer;

import java.util.*;

public abstract class BFSSyncAgent extends SyncAgent {

    public static final int MSG_TYPE_LAYER = 0XFFFBF0;
    public static final int MSG_TYPE_ACK = 0XFFFBF1;

    protected int parent;
    protected int level;
    protected List<Integer> children;
    protected List<Integer> pseudoParent;
    protected List<Integer> pseudoChildren;
    protected List<Integer> siblings;
    protected HashSet<Integer> receivedLayerMsg;
    protected HashSet<Integer> receivedACKMsg;


    public BFSSyncAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
        children = new LinkedList<>();
        pseudoParent = new LinkedList<>();
        pseudoParent = new LinkedList<>();
        siblings = new LinkedList<>();
        receivedLayerMsg = new HashSet<>();
        receivedACKMsg = new HashSet<>();
    }

    @Override
    protected void initRun() {
        level = Integer.MAX_VALUE;
        if (this.id == 1){
            level = 0;
            for (int neighborId:neighbors) {
                children.add(neighborId);
            }
            sendLayerMessage(children,level);
        }
        
    }
    private void sendLayerMessage(List<Integer> childrenList,int parentLevel) {
        for (int childId:childrenList) {
            Message message = new Message(this.id,childId,MSG_TYPE_LAYER,parentLevel);
            sendMessage(message);
        }
    }

    protected boolean isRootAgent(){
        return parent <= 0;
    }

    protected boolean isLeafAgent(){
        return children.size() == 0;
    }

    @Override
    public void disposeMessage(Message message) {
        switch (message.getMsgType()){
            case MSG_TYPE_LAYER:
                disposeLayerMessage(message);
                break;
            case MSG_TYPE_ACK:
                disposeACKMessage(message);
                break;
        }
    }

    private void disposeACKMessage(Message message) {
        receivedACKMsg.add(message.getIdSender());
        if (receivedACKMsg.size() == this.neighbors.length){
            runFinished();
        }
    }

    private void disposeLayerMessage(Message message) {
        receivedLayerMsg.add(message.getIdSender());
        if ((int)(message.getValue())+ 1 < this.level){
            this.level = (int)(message.getValue())+ 1;
            this.parent = message.getIdSender();
            for (int neighborId:neighbors) {
                if (neighborId != this.parent){
                    children.add(neighborId);
                }
            }
            sendLayerMessage(children,this.level);
            sendACKMessage(this.parent);
        }else {
            if ((int)(message.getValue()) == this.level){
                siblings.add(message.getIdSender());
                children.remove(message.getIdSender());
            }
            if ((int)(message.getValue()) < this.level){
                pseudoParent.add(message.getIdSender());
                sendACKMessage(message.getIdSender());
                children.remove(message.getIdSender());
            }
        }
        if (receivedLayerMsg.size() == this.neighbors.length){
            assignPseudoChildren();
            runFinished();
        }
    }

    private void assignPseudoChildren() {
        for (int neighbourId : neighbors){
            if (neighbourId != parent && !children.contains(neighbourId) && !pseudoParent.contains(neighbourId) && !siblings.contains(neighbourId)){
                pseudoChildren.add(neighbourId);
            }
        }
    }

    private void sendACKMessage(int parentId) {
        Message message = new Message(this.id,parentId,MSG_TYPE_ACK,null);
        sendMessage(message);
    }
}
