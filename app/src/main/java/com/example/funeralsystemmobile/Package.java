package com.example.funeralsystemmobile;

public class Package {
    private int id;
    private String name;
    private String img;
    private String description;
    private String inclusions;
    private String category;
    private int price;

    public Package() {
    }

    public Package(int id, String name, String img, String description, String inclusions, String category, int price) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.description = description;
        this.inclusions = inclusions;
        this.category = category;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
