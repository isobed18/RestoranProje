package org.restoranproje.model;

public class StockItem {
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
} 
