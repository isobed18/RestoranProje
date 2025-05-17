package org.restoranproje.service;

import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;
import org.restoranproje.db.OrderDAO;
import java.util.List;

public class OrderService {
    private static OrderService instance;

    private OrderService() {
        // Private constructor for singleton pattern
    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public List<Order> getAllOrders() {
        return OrderDAO.getActiveOrders();
    }

    public void updateOrder(Order order) {
        // Log the order update in history
        OrderDAO.logOrderHistory(order);
        
        // If the order is completed, save it to completed_orders
        if (order.getStatus() == OrderStatus.COMPLETED) {
            OrderDAO.saveCompletedOrder(order);
        }
    }

    public List<Order> getOrderHistory() {
        return OrderDAO.getFullOrderHistory();
    }
} 