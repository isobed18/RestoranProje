package org.restoranproje.model;

public class RecipeIngredient {
    private StockItem stockItem;
    private double amount; // gram, ml, adet gibi

    public RecipeIngredient(StockItem stockItem, double amount) {
        this.stockItem = stockItem;
        this.amount = amount;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
