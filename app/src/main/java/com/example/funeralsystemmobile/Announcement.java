package com.example.funeralsystemmobile;

public class Announcement {
    private int id;
    private String img;
    private String description;

    public Announcement() {
    }

    public Announcement(int id, String img, String description) {
        this.id = id;
        this.img = img;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
