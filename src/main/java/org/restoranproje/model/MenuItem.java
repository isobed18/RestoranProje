package org.restoranproje.model;

import java.util.ArrayList;


 public class MenuItem{
    private String name;
    private String description;
    private MenuItemType type;
    private ArrayList<StockItem> items;
    int price;

    public MenuItem(String name, String description,MenuItemType type, int price, ArrayList<StockItem> items) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.items = items;

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
    public ArrayList<StockItem> getItems() {
        return items;
    }


 }
