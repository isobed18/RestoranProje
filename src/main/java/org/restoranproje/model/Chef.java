package org.restoranproje.model;

import org.restoranproje.service.OrderManager;

public class Chef extends User {
    public Chef(String name) {
        super(name);
    }

    @Override
    public void update(Order order) {
        System.out.println("Chef " + name + " notified: " + order);
    }

    public void prepareOrder(OrderManager manager, int orderId) {
        manager.updateOrderStatus(orderId, OrderStatus.PREPARING);
    }

    public void completeOrder(OrderManager manager, int orderId) {
        manager.updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }
}
