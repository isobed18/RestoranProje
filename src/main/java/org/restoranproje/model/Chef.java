package org.restoranproje.model;

import org.restoranproje.db.UserDAO;
import org.restoranproje.service.OrderManager;

public class Chef extends User {
    public Chef(String name,String password) {
        super(name,UserType.CHEF, password);
        UserDAO.saveUser(this);
    }

    public Chef(String name, String password, boolean saveToDB) {
        super(name, UserType.CHEF, password);
        if (saveToDB) {
            UserDAO.saveUser(this);
        }
    }

    @Override
    public void update(Order order) {
        System.out.println("Chef " + name + " notified: " + order);
    }
    
    public void completeOrder(OrderManager manager, int orderId) {
        manager.updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }
}
