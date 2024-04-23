package com.example.funeralsystemmobile;

public class OrderItem {
    private int id;
    private int orderId;
    private String name;
    private String image;
    private int price;
    private int quantity;

    public OrderItem(int id, int orderId, String name, String image, int price, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
