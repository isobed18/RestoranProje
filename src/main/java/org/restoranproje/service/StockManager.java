package org.restoranproje.service;

import org.restoranproje.db.StockDAO;
import org.restoranproje.model.Observer;
import org.restoranproje.model.Order;
import org.restoranproje.model.StockItem;
import org.restoranproje.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class StockManager {
    private List<Observer> observers = new ArrayList<>();
    private List<StockItem> stockItems = new ArrayList<>();
    private StockDAO stockDAO = new StockDAO();

    public StockManager() {
        stockItems.addAll(stockDAO.getAllStockItems());
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Order order) {
        for (Observer obs : observers) {
            obs.update(order);
        }
    }

    public void addStockItem(StockItem item) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getName().equals(item.getName())) {
                stockItem.setAmount(stockItem.getAmount() + item.getAmount());
                stockDAO.updateStockItem(stockItem);
                return;
            }
        }
        stockItems.add(item);
        stockDAO.insertStockItem(item);
    }

    public void removeStockItem(StockItem item) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getName().equals(item.getName())) {
                stockItem.setAmount(stockItem.getAmount() - item.getAmount());
                stockDAO.updateStockItem(stockItem);
            }
        }
    }

    public boolean canFulfillOrder(Order order) {
        for (MenuItem item : order.getItems()) {
            for (StockItem needed : item.getItems()) {
                StockItem stockItem = findStockItemByName(needed.getName());
                if (stockItem == null || stockItem.getAmount() < needed.getAmount()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void fulfillOrder(Order order) {
        for (MenuItem item : order.getItems()) {
            for (StockItem needed : item.getItems()) {
                StockItem stockItem = findStockItemByName(needed.getName());
                double newAmount = stockItem.getAmount() - needed.getAmount();
                stockItem.setAmount(newAmount);
                stockDAO.updateStockItem(stockItem);
            }
        }
    }

    private StockItem findStockItemByName(String name) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getName().equals(name)) {
                return stockItem;
            }
        }
        return null;
    }

    public void restoreStockForOrder(Order order) {
        for (MenuItem item : order.getItems()) {
            for (StockItem used : item.getItems()) {
                StockItem stock = findStockItemByName(used.getName());
                if (stock != null) {
                    stock.setAmount(stock.getAmount() + used.getAmount());
                    stockDAO.updateStockItem(stock);
                }
            }
        }
    }

    public void printAllStock() {
        for (StockItem stockItem : stockItems) {
            System.out.println("name: " + stockItem.getName() + "/amount: " + stockItem.getAmount() +
                    "/unit: " + stockItem.getUnit() + "/unitCost: " + stockItem.getUnitCost());
        }
    }
} 