package org.restoranproje.model;
//deneme
import org.restoranproje.db.MenuDAO;
import org.restoranproje.db.StockDAO;
import org.restoranproje.db.UserDAO;
import org.restoranproje.service.OrderManager;
import org.restoranproje.service.StockManager;

import java.util.List;

public class Manager extends User {
    public Manager(String name, String password) {
        super(name, UserType.MANAGER, password);
        UserDAO.saveUser(this);
    }

    @Override
    public void update(Order order) {
        System.out.println("Manager " + name + " notified: " + order);
    }

    public void viewAllOrders(OrderManager manager) {
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

    public void addStockItem(StockManager stockManager, StockItem item) {
        stockManager.addStockItem(item);
    }

    public void addMenuItem(MenuDAO menuDAO, MenuItem menuItem, int menuItemId, List<Integer> stockItemIds) {
        menuDAO.insertMenuItem(menuItem, menuItemId, stockItemIds);
    }

    public void removeStockItem(StockDAO stockDAO, String stockItemName) {
        stockDAO.removeStockItemByName(stockItemName);
    }

    public void removeMenuItem(MenuDAO menuDAO, String menuItemName) {
        menuDAO.removeMenuItemByName(menuItemName);
    }

    public void changeUnitCost(StockDAO stockDAO, String stockItemName, double newUnitCost) {
        stockDAO.updateUnitCost(stockItemName, newUnitCost);
    }
} 
