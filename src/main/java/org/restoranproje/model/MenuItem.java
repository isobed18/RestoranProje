package org.restoranproje.model;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private MenuItemType type;
    private double price;
    private List<StockItem> items;

    public MenuItem(String name, String description, MenuItemType type, double price, List<StockItem> items) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.items = items;
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

    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<StockItem> getItems() {
        return items;
    }

    public void setItems(List<StockItem> items) {
        this.items = items;
    }

    public double getCost() {
        double totalCost = 0;
        if (items != null) {
            for (StockItem item : items) {
                totalCost += item.getAmount() * item.getUnitCost();
            }
        }
        return totalCost;
    }

    @Override
    public String toString() {
        return name + " (" + price + " TL)";
    }
} 