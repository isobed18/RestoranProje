package org.restoranproje.service;

import org.restoranproje.model.Observer;
import org.restoranproje.model.Order;
import org.restoranproje.model.StockItem;
import org.restoranproje.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class StockManager {
    private List<Observer> observers = new ArrayList<>();
    private List<StockItem> stockItems = new ArrayList<>();


    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Order order) { // order bildirimi
        for (Observer obs : observers) {
            obs.update(order); // order geldiğinde (burada herhangi bir değişiklik olunca order geliyor)
            // observerlara bildirim düşüyor
        }
    }
    public void addStockItem(StockItem item) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getName().equals(item.getName())) {
                stockItem.setCount(stockItem.getCount() + item.getCount()); // üzerine ekle
                return;
            }
        }
        stockItems.add(item); // yeni ürünse ekle
    }
    public void removeStockItem(StockItem item) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getName().equals(item.getName())) {
                stockItem.setCount(stockItem.getCount() - item.getCount());
            }
        }
    }
    public boolean canFulfillOrder(Order order) {
        for (MenuItem item : order.getItems()) {
            for (StockItem needed : item.getItems()) {
                StockItem stockItem = findStockItemByName(needed.getName());
                if (stockItem == null || stockItem.getCount() < needed.getCount()) {
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
                int newCount = stockItem.getCount() - needed.getCount();
                stockItem.setCount(newCount);
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
                    stock.setCount(stock.getCount() + used.getCount());
                }
            }
        }
    }
    public void printAllStock() {
        for (StockItem stockItem : stockItems) {
            System.out.println("name: " + stockItem.getName() + "/count: " + stockItem.getCount() +
                    "/detail: " + stockItem.getDescription());

        }
    }



}
