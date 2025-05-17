package org.restoranproje.service;

import org.restoranproje.db.OrderDAO;
import org.restoranproje.model.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private List<Observer> observers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private StockManager smanager = new StockManager();

    public OrderManager() {
        loadActiveOrders();
    }

    private void loadActiveOrders() {
        List<Order> activeOrdersFromDB = OrderDAO.getActiveOrders();
        if (activeOrdersFromDB != null) {
            this.orders.addAll(activeOrdersFromDB);
            System.out.println("Veritabanından " + activeOrdersFromDB.size() + " aktif sipariş yüklendi.");
        } else {
            System.out.println("Veritabanından aktif sipariş yüklenemedi veya hiç aktif sipariş yok.");
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        smanager.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
        smanager.removeObserver(observer);
    }

    public StockManager getStockManager() {
        return smanager;
    }

    public void notifyObservers(Order order) {
        for (Observer obs : observers) {
            obs.update(order);
        }
    }

    public Order findOrderById(int id) {
        for (Order o : orders) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public void addOrder(Order order) {
        if (order.getStatus() != OrderStatus.CANCELLED && smanager.canFulfillOrder(order)) {
            // Insert order into DB and get generated ID
            int generatedId = org.restoranproje.Main.insertNewOrder(order.getDetails(), order.getStatus());
            if (generatedId > 0) {
                order.setId(generatedId);
            }
            smanager.fulfillOrder(order);
            orders.add(order);
            OrderDAO.logOrderHistory(order);
            notifyObservers(order);
        } else if (order.getStatus() == OrderStatus.CANCELLED) {
            int generatedId = org.restoranproje.Main.insertNewOrder(order.getDetails(), order.getStatus());
            if (generatedId > 0) {
                order.setId(generatedId);
            }
            orders.add(order);
            OrderDAO.logOrderHistory(order);
            notifyObservers(order);
        } else {
            System.out.println("Stok yetersiz! Sipariş alınamadı.");
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);
        if (order != null) {
            // Prevent invalid status changes
            if (order.getStatus() == OrderStatus.CANCELLED) {
                System.out.println("Bu sipariş zaten iptal edilmiş!");
                return;
            }
            if (order.getStatus() == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
                System.out.println("Teslim edilmiş siparişin durumu değiştirilemez!");
                return;
            }
            if (newStatus == OrderStatus.CANCELLED && order.getStatus() == OrderStatus.DELIVERED) {
                System.out.println("Teslim edilmiş sipariş iptal edilemez!");
                return;
            }

            order.setStatus(newStatus);
            OrderDAO.logOrderHistory(order);
            
            if (newStatus == OrderStatus.DELIVERED) {
                OrderDAO.saveCompletedOrder(order);
            }
            if (newStatus == OrderStatus.CANCELLED) {
                smanager.restoreStockForOrder(order);
            }
            
            notifyObservers(order);
            System.out.println("Durum güncellendi: " + order);
        } else {
            System.out.println("Order not found");
        }
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == status) {
                filtered.add(order);
            }
        }
        return filtered;
    }
}
