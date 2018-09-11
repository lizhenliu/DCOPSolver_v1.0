package edu.cqu.ordering;

import edu.cqu.core.Message;
import edu.cqu.core.SyncAgent;
import edu.cqu.core.SyncMailer;

import java.util.*;

public abstract class DFSSyncAgent extends SyncAgent {

    public static final int MSG_TYPE_DFS_LAYER = 0XFFFDF0;
    public static final int MSG_TYPE_DFS_BACKTRACK = 0XFFFDF1;
    public static final int MSG_TYPE_DFS_ACK = 0XFFFDF2;
    public static final int MSG_TYPE_DFS_GO = 0XFFFDF3;

    protected int parent;
    protected int child;
    protected int level;
    protected boolean isLeaf;
    protected boolean isRoot;
    protected List<Integer> pseudoChildren;
    protected List<Integer> pseudoParent;
    protected List<Integer> siblings;
    protected Set<Integer> unVisitedSet;

    public DFSSyncAgent(int id, int[] domain, int[] neighbours, Map<Integer, int[][]> constraintCosts, Map<Integer, int[]> neighbourDomains, SyncMailer mailer) {
        super(id, domain, neighbours, constraintCosts, neighbourDomains, mailer);
        pseudoChildren = new LinkedList<>();
        pseudoParent = new LinkedList<>();
        siblings = new LinkedList<>();
        unVisitedSet = new HashSet<>();
    }

    @Override
    protected void initRun() {
        level = Integer.MAX_VALUE;
        child = -1;
        parent = -1;
        isRoot = false;
        isLeaf = false;
        for (int neighborId:neighbors) {
            unVisitedSet.add(neighborId);
        }
        if (this.id == 1){
            isRoot = true;
            level = 0;
            child = (int) (Math.random()*neighbors.length);
            sendDFSLayerMessage(child,level);
        }
    }

    private void sendDFSLayerMessage(int childId,int level) {
        Message message = new Message(this.id,childId,MSG_TYPE_DFS_LAYER,level);
        sendMessage(message);
    }

    @Override
    public void disposeMessage(Message message) {
        switch (message.getMsgType()){
            case MSG_TYPE_DFS_LAYER:
                disposeLayerMessage(message);
                break;
            case MSG_TYPE_DFS_BACKTRACK:
                disposeBackTrackMessage(message);
                break;
            case MSG_TYPE_DFS_ACK:
                disposeAckMessage(message);
                break;
            case MSG_TYPE_DFS_GO:
                disposeGoMessage(message);
                break;
        }
    }

    private void disposeGoMessage(Message message) {

        if (!unVisitedSet.isEmpty()){
            int tempChild = (int) (Math.random()*neighbors.length);
            while(!unVisitedSet.contains(tempChild)){
                tempChild = (int) (Math.random()*neighbors.length);
                }
                sendDFSLayerMessage(tempChild,this.level);
            }else {
            //安排伪父子节点
            sendBackTrackMessage(message.getIdSender());
            runFinished();
        }
    }

    private void disposeAckMessage(Message message) {
        sendGoMessage(message.getIdSender());
    }

    private void sendGoMessage(int index) {
        Message message = new Message(this.id,index,MSG_TYPE_DFS_GO,null);
        sendMessage(message);
    }

    private void disposeLayerMessage(Message message) {
        int recLevel = (int) message.getValue();
        if (parent == -1){
            if (this.level < recLevel + 1){
                unVisitedSet.remove(message.getIdSender());
                parent = message.getIdSender();
                sendAckMessage(parent);
            }else if (this.level == recLevel){
                unVisitedSet.remove(message.getIdSender());
                siblings.add(message.getIdSender());
                sendBackTrackMessage(message.getIdSender());
            }else {
                unVisitedSet.remove(message.getIdSender());
                pseudoChildren.add(message.getIdSender());//
            }
        }else {
            unVisitedSet.remove(message.getIdSender());
            sendBackTrackMessage(message.getIdSender());
            if (level < recLevel){
                pseudoChildren.add(message.getIdSender());
            }else {
                pseudoParent.add(message.getIdSender());
            }
        }
    }

    private void sendBackTrackMessage(int index) {
        Message message = new Message(this.id,index,MSG_TYPE_DFS_BACKTRACK,null);
        sendMessage(message);
    }

    private void sendAckMessage(int parentIndex) {
        Message message = new Message(this.id,parentIndex,MSG_TYPE_DFS_ACK,null);
        sendMessage(message);
    }

    private void disposeBackTrackMessage(Message message) {
        unVisitedSet.remove(message.getIdSender());
        if (!unVisitedSet.isEmpty()){
            int tempChild = (int) (Math.random()*neighbors.length);
            while(!unVisitedSet.contains(tempChild)){
                tempChild = (int) (Math.random()*neighbors.length);
                }
                sendDFSLayerMessage(tempChild,this.level);
            }else {
            runFinished();
            if (!isRoot){
                sendBackTrackMessage(message.getIdSender());//安排伪父子节点
            }else {
                //建树完成
            }
            }
    }

    public String toDOTString(){
        StringBuilder stringBuilder = new StringBuilder();
        if (parent > 0){
            stringBuilder.append("X" + parent + "->X" + id + ";\n");
        }
        for (int pp : pseudoParent){
            stringBuilder.append("X" + pp + "->X" + id + " [style=dotted];\n");
        }
        return stringBuilder.toString();
    }

}
