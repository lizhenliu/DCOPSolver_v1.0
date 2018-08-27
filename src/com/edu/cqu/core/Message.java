package com.edu.cqu.core;

public class Message {
    private int idSender;
    private int idReceiver;
    private int msgType;
    private Object value;

    public Message(int idSender, int idReceiver, int msgType, Object value) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.msgType = msgType;
        this.value = value;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
