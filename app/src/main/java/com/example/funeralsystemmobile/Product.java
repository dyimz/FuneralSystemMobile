package com.example.funeralsystemmobile;

public class Product {
    private int id;
    private String name;
    private String description;
    private String details;
    private int price;
    private String img;
    private String category;
    private int stock;

    public Product() {
    }

    public Product(int id, String name, String description, String details, int price, String img, String category, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.details = details;
        this.price = price;
        this.img = img;
        this.category = category;
        this.stock = stock;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
