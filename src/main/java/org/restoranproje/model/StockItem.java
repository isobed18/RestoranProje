package org.restoranproje.model;

public class StockItem {
    private int id;
    private String name;
    private double amount;
    private String unit;
    private double unitCost;

    public StockItem(String name, double amount, String unit, double unitCost) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.unitCost = unitCost;
    }

    public StockItem(int id, String name, double amount, String unit, double unitCost) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.unitCost = unitCost;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", " + name + " (" + amount + " " + unit + ")";
    }
} 
