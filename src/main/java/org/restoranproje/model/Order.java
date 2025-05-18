package org.restoranproje.model;

import java.util.ArrayList;

public class Order {
    private int id;
    private String details;
    private OrderStatus status;
    private ArrayList<MenuItem> Items;

    public Order(int id, String details, ArrayList<MenuItem> Items) {
        this.id = id;
        this.details = details;
        this.Items = Items;
        this.status = OrderStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ArrayList<MenuItem> getItems() {
        return Items;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.Items = items;
    }

    @Override
    public String toString() {
        return "order ID:" + id + " - " + details + " [" + status + "]";
    }
} 
