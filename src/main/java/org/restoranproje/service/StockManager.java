package org.restoranproje.service;

import org.restoranproje.db.StockDAO;
import org.restoranproje.model.*;

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
        StockItem existingItem = findStockItemByName(item.getName());
        if (existingItem != null) {
            existingItem.setAmount(existingItem.getAmount() + item.getAmount());
            stockDAO.updateStockItem(existingItem);
        } else {
            stockItems.add(item);
            stockDAO.insertStockItem(item);
        }
    }

    public void removeStockItem(StockItem item) {
        StockItem existingItem = findStockItemByName(item.getName());
        if (existingItem != null) {
            double newAmount = existingItem.getAmount() - item.getAmount();
            if (newAmount < 0) {
                newAmount = 0;
            }
            existingItem.setAmount(newAmount);
            stockDAO.updateStockItem(existingItem);
        }
    }

    public boolean canFulfillOrder(Order order) {
        for (MenuItem item : order.getItems()) {
            for (StockItem needed : item.getItems()) {
                StockItem stockItem = findStockItemById(needed.getId());
                if (stockItem == null || stockItem.getAmount() < needed.getAmount()) {
                    System.out.println("Stok yetersiz: " + stockItem.getName() + 
                                     " (Gereken: " + needed.getAmount() + 
                                     ", Mevcut: " + (stockItem != null ? stockItem.getAmount() : 0) + ")");
                    return false;
                }
            }
        }
        return true;
    }

    public void fulfillOrder(Order order) {
        if (!canFulfillOrder(order)) {
            System.out.println("Sipariş yerine getirilemedi: Yetersiz stok!");
            return;
        }

        for (MenuItem item : order.getItems()) {
            for (StockItem needed : item.getItems()) {
                StockItem stockItem = findStockItemById(needed.getId());
                if (stockItem != null) {
                    double newAmount = stockItem.getAmount() - needed.getAmount();
                    if (newAmount < 0) {
                        System.out.println("Hata: Stok negatife düştü! " + stockItem.getName());
                        continue;
                    }
                    stockItem.setAmount(newAmount);
                    stockDAO.updateStockItem(stockItem);
                }
            }
        }
    }

    private StockItem findStockItemById(int id) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getId() == id) {
                return stockItem;
            }
        }
        return null;
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
        // Restore stock when order is cancelled
        if (order.getStatus() == OrderStatus.CANCELLED) {
            for (MenuItem item : order.getItems()) {
                for (StockItem used : item.getItems()) {
                    StockItem stock = findStockItemById(used.getId());
                    if (stock != null) {
                        // Get the current stock from database to ensure accuracy
                        StockItem currentStock = stockDAO.getStockItemById(stock.getId());
                        if (currentStock != null) {
                            currentStock.setAmount(currentStock.getAmount() + used.getAmount());
                            stockDAO.updateStockItem(currentStock);
                            // Update the in-memory stock item
                            stock.setAmount(currentStock.getAmount());
                        }
                    }
                }
            }
        }
    }

    public void printAllStock() {
        for (StockItem stockItem : stockItems) {
            System.out.println("ID: " + stockItem.getId() + 
                             ", name: " + stockItem.getName() + 
                             ", amount: " + stockItem.getAmount() +
                             ", unit: " + stockItem.getUnit() + 
                             ", unitCost: " + stockItem.getUnitCost());
        }
    }

    public List<StockItem> getAllStockItems() {
        return stockItems;
    }
} 