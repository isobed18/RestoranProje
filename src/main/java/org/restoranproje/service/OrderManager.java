package org.restoranproje.service;


import org.restoranproje.db.OrderDAO;
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

    private Order findOrderById(int id) { // verilen id ile orders listesinde orderı bulacak
        for (Order o : orders) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public void addOrder(Order order) { // order ekleme
        orders.add(order); // ram

        OrderDAO.logOrderHistory(order); // db
        notifyObservers(order); // notify
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);
        if (order != null) {
            order.setStatus(newStatus); // Sipariş nesnesini güncelle

            OrderDAO.logOrderHistory(order); // herhangi bir değişiklikte orderhistorye logla

            if (newStatus == OrderStatus.DELIVERED) {
                OrderDAO.saveCompletedOrder(order); // delivered olanları completed ordersa ekle
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
