package org.restoranproje.model;

import org.restoranproje.service.OrderManager;
import org.restoranproje.db.UserDAO;
public class Waiter extends User {
    public Waiter(String name, String password) {
        super(name,UserType.WAITER, password);
        UserDAO.saveUser(this);
    }

    @Override
    public void update(Order order) {
        System.out.println("Waiter " + name + " notified: " + order);
    }

    public void takeOrder(OrderManager manager, int id, String details) {
        Order order = new Order(id, details);
        manager.addOrder(order);
        System.out.println("Waiter " + name + " added order: " + order.getDetails());
    }

    public void deliverOrder(OrderManager manager, int orderId) {
        manager.updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }
}
