package com.example.funeralsystemmobile;

public class Order {
    private int id;
    private int customerID;
    private String name;
    private String address;
    private String contact;
    private int total;
    private String MOP;
    private String POP;
    private String type;
    private String status;
    private String created_at;

    public Order() {
    }

    public Order(int id, int customerID, String name, String address, String contact, int total, String MOP, String POP, String type, String status, String created_at) {
        this.id = id;
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.total = total;
        this.MOP = MOP;
        this.POP = POP;
        this.type = type;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMOP() {
        return MOP;
    }

    public void setMOP(String MOP) {
        this.MOP = MOP;
    }

    public String getPOP() {
        return POP;
    }

    public void setPOP(String POP) {
        this.POP = POP;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
