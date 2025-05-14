package org.restoranproje.service;


import org.restoranproje.db.OrderDAO;
import org.restoranproje.model.*;
import org.restoranproje.model.Observer;

import java.util.ArrayList;
import java.util.List;
import org.restoranproje.service.StockManager;
import java.util.*;

public class OrderManager {
    private List<Observer> observers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private StockManager smanager = new StockManager();

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

    public void notifyObservers(Order order) { // order bildirimi
        for (Observer obs : observers) {
            obs.update(order); // order geldiğinde (burada herhangi bir değişiklik olunca order geliyor)
                                // observerlara bildirim düşüyor
        }
    }

    public Order findOrderById(int id) { // verilen id ile orders listesinde orderı bulacak
        for (Order o : orders) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public void addOrder(Order order) {
        if (smanager.canFulfillOrder(order)) {
            smanager.fulfillOrder(order); // stokları azalt
            orders.add(order); // belleğe ekle
            OrderDAO.logOrderHistory(order); // veritabanına logla
            notifyObservers(order); // observer'lara bildir
        } else {
            System.out.println("Stok yetersiz! Sipariş alınamadı.");

        }
    }


    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(newStatus); // Sipariş nesnesini güncelle

            OrderDAO.logOrderHistory(order); // herhangi bir değişiklikte orderhistorye logla

            if (newStatus == OrderStatus.DELIVERED) {
                OrderDAO.saveCompletedOrder(order); // delivered olanları completed ordersa ekle
            }
            if (newStatus == OrderStatus.CANCELLEDBP) {
                smanager.restoreStockForOrder(order);
            }


            notifyObservers(order); // bildirim
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
