package org.restoranproje.service;


import org.restoranproje.model.Observer;
import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;
import java.util.ArrayList;
import java.util.List;

import java.util.*;

public class OrderManager {
    private List<Observer> observers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

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

    public void addOrder(Order order) { // order ekleme
        orders.add(order);
        notifyObservers(order);
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) { // order durumu değiştirme
        for (Order order : orders) {
            if (order.getId() == orderId) {
                order.setStatus(newStatus);
                notifyObservers(order);  // yeni durumu observerlara bildirme
                return;
            }
        }
        System.out.println("Order ID not found: " + orderId);  //order id yoksa
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
