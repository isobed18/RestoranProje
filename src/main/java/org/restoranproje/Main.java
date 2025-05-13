package org.restoranproje;

import org.restoranproje.service.*;
import org.restoranproje.model.*;
import org.restoranproje.db.*;
public class Main {
    public static void main(String[] args) {
       // DatabaseManager.connect();
        OrderManager manager = new OrderManager();

        Waiter waiter = new Waiter("İshak");
        Chef chef = new Chef("Coşkun");
        Manager admin = new Manager("Yusuf");

        manager.addObserver(waiter);
        manager.addObserver(chef);
        manager.addObserver(admin);

        waiter.takeOrder(manager, 1,"1 adet pizza");

        admin.viewOrderByStatus(manager,OrderStatus.NEW); // manager new statusundeki ürünlere bakıyor

        chef.prepareOrder(manager, 1);
        admin.viewOrderByStatus(manager,OrderStatus.PREPARING); // manager new statusundeki ürünlere bakıyor

        chef.completeOrder(manager, 1);

        waiter.deliverOrder(manager, 1);

        System.out.println("\nManager View"); // manager tüm ürünlere bakıyor
        admin.viewAllOrders(manager);

    }
}
