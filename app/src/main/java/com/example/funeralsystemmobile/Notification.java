package com.example.funeralsystemmobile;

public class Notification {
    private int id;
    private String description;

    public Notification() {
    }

    public Notification(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
