package org.restoranproje.model;

public class StockItem{
    private String name;
    private String description;
    private int count;
    private int price;
    public StockItem(String name, String description, int count,int price) {
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
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}