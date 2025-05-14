package org.restoranproje.model;
import org.restoranproje.db.UserDAO;
import org.restoranproje.service.OrderManager;
public class Manager extends User {
    public Manager(String name,String password) {
        super(name,UserType.MANAGER, password);
        UserDAO.saveUser(this);
    }

    @Override
    public void update(Order order) { // Usera order bildirme
        System.out.println("Manager " + name + " notified: " + order); //şimdilik buradan alıyor bildirimi
    }

    public void viewAllOrders(OrderManager manager) { //tüm orderları görme
        for (Order o : manager.getAllOrders()) {
            System.out.println(o);
        }
    }
    public void viewOrderByStatus(OrderManager manager, OrderStatus status) {
        for (Order o : manager.getOrdersByStatus(status)) {
            System.out.println(o);
        }
    }
    public void changeOrderStatus(OrderManager manager, int orderId, OrderStatus status) {
        manager.updateOrderStatus(orderId, status);
    }
}
