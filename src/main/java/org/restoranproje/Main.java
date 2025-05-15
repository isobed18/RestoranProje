package org.restoranproje;

import org.restoranproje.service.*;
import org.restoranproje.model.*;
import org.restoranproje.db.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        DatabaseManager.setupDatabase(); // Kurarken bir kez çalıştırın

        // Yöneticiler ve servisler
        OrderManager orderManager = new OrderManager();
        StockManager stockManager = orderManager.getStockManager();

        Waiter waiter = new Waiter("İshak", "123");
        Chef chef = new Chef("Coşkun", "1234");
        Manager admin = new Manager("Yusuf", "1234");

        // Observer ekleniyor
        orderManager.addObserver(waiter);
        orderManager.addObserver(chef);
        orderManager.addObserver(admin);

        // Başlangıç stoğu
        StockItem etStok = new StockItem(1,"et", "dana eti kg, kg başı fiyat", 100, 650);
        StockItem otStok = new StockItem(2,"ot","ot stok", 100, 10);
        stockManager.addStockItem(etStok);  // StokManager'a ürün ekleniyor
        stockManager.addStockItem(otStok);
        System.out.println("Stoklar:");
        stockManager.printAllStock();

        // Siparişe ait stok gereksinimi

        StockItem etDonerdekiet = new StockItem(3,"et","etdonerdeki et", 1, 650);
        StockItem etDonerdekiot = new StockItem(4,"ot","etdonerdeki", 10, 10);
        ArrayList<StockItem> etdoner_items = new ArrayList<>();
        etdoner_items.add(etDonerdekiet);
        etdoner_items.add(etDonerdekiot);
        MenuItem etdoner = new MenuItem("etdoner","et doner",MenuItemType.DISH,
                1000,etdoner_items);

        ArrayList<MenuItem> orderItems = new ArrayList<>();
        orderItems.add(etdoner);
        Order order1 = new Order(1,"ornek detay", orderItems);
        orderManager.addOrder(order1);
        System.out.println("stoklar:");
        stockManager.printAllStock();

        System.out.println("tüm siparişler:");
        admin.viewAllOrders(orderManager);

        otStok.setCount(90);
        stockManager.removeStockItem(otStok);
        // İkinci sipariş testi
        stockManager.printAllStock();
        order1.setId(2);
        waiter.takeOrder(orderManager, order1); // stok yetersiz olmalı şuan
        StockItem otStok2 = new StockItem(5,"ot","ot stok", 100, 10);
        stockManager.printAllStock();

        stockManager.addStockItem(otStok2);
        stockManager.printAllStock();
        waiter.takeOrder(orderManager, order1); //şimdi alınmalı
        stockManager.printAllStock();


        // stok restore
        System.out.println("Sipariş iptal testi :");

        orderManager.updateOrderStatus(2, OrderStatus.CANCELLEDBP);
        stockManager.printAllStock();


    }
}
