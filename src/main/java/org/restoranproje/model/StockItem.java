package org.restoranproje.model;

public class StockItem {

    private int id;
    private String name;
    private String description;
    private double count;
    private int price;

    public StockItem(int id, String name, String description, double count, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.count = count;
        this.price = price;
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

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}