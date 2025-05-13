package org.restoranproje.model;

public class Order {
    private int id;
    private String details;
    private OrderStatus status;

    public Order(int id, String details) {
        this.id = id;
        this.details = details;
        this.status = OrderStatus.NEW;
    }

    public int getId() { return id; }
    public String getDetails() { return details; }
    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "order ID:" + id + " - " + details + " [" + status + "]";
    }
}
