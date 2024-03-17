package com.example.funeralsystemmobile;

public class Message {
    private int fromId;
    private int toId;
    private String body;

    public Message() {
    }

    public Message(int fromId, int toId, String body) {
        this.fromId = fromId;
        this.toId = toId;
        this.body = body;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}