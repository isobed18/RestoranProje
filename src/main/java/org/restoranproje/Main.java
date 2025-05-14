package org.restoranproje;

import org.restoranproje.service.*;
import org.restoranproje.model.*;
import org.restoranproje.db.*;
public class Main {
    public static void main(String[] args) {


        DatabaseManager.setupDatabase(); //kurarken bir kez çalıştırın
        OrderManager manager = new OrderManager();

        Waiter waiter = new Waiter("İshak","123");
        Chef chef = new Chef("Coşkun","1234");
        Manager admin = new Manager("Yusuf","1234");

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


        waiter.takeOrder(manager, 2,"cay");
        manager.updateOrderStatus(2, OrderStatus.DELIVERED);
        /* NOT: intelliJ database kısmında restoran.db'ye basın, jump to query console
        ve aşağıdakileri yazıp execute edince tabloların içi sıfırlanacak
        daha temiz çalışmak için testlerden sonra yapılmalı
            DELETE FROM completed_orders
            DELETE FROM order_history
         */
    }
}
