package org.restoranproje;

import org.restoranproje.db.DatabaseManager;
import org.restoranproje.db.StockDAO;
import org.restoranproje.db.MenuDAO;
import org.restoranproje.model.*;
import org.restoranproje.service.OrderManager;
import org.restoranproje.service.StockManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Veritabanı yapısını oluştur
        DatabaseManager.setupDatabase();

        // Yöneticileri ve sistemi başlat
        OrderManager orderManager = new OrderManager();
        StockManager stockManager = orderManager.getStockManager();
        StockDAO stockDAO = new StockDAO();
        MenuDAO menuDAO = new MenuDAO();

        // Kullanıcıları oluştur
        Waiter garson = new Waiter("Zeynep", "1234");
        Chef sef = new Chef("Ahmet", "abcd");
        Manager mudur = new Manager("Emir", "admin");

        // 1. Stokları oluştur (Yönetici yapar)
        mudur.addStockItem(stockManager, new StockItem("Et", 1000.0, "gram", 0.25));
        mudur.addStockItem(stockManager, new StockItem("Domates", 50.0, "adet", 2.0));
        mudur.addStockItem(stockManager, new StockItem("Soğan", 30.0, "adet", 1.5));

        System.out.println("\n✅ RAM'deki Stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n✅ Veritabanındaki Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }

        // 2. Menü ürünlerini oluştur (Yönetici yapar)
        ArrayList<StockItem> adanaIngredients = new ArrayList<>(Arrays.asList(
            new StockItem("Et", 200.0, "gram", 0.25),
            new StockItem("Domates", 1.0, "adet", 2.0)
        ));

        ArrayList<StockItem> donerIngredients = new ArrayList<>(Arrays.asList(
            new StockItem("Et", 150.0, "gram", 0.25),
            new StockItem("Soğan", 1.0, "adet", 1.5)
        ));

        MenuItem adana = new MenuItem("Adana Kebap", "Acılı kebap", MenuItemType.DISH, 80.0, adanaIngredients);
        MenuItem doner = new MenuItem("Et Döner", "Klasik döner", MenuItemType.DISH, 70.0, donerIngredients);

        mudur.addMenuItem(menuDAO, adana, 1, Arrays.asList(1, 2));
        mudur.addMenuItem(menuDAO, doner, 2, Arrays.asList(1, 3));

        // 3. Garson sipariş alıyor
        ArrayList<MenuItem> siparis1 = new ArrayList<>(Arrays.asList(adana));
        Order order1 = new Order(1, "Masa 5 - Adana Kebap", siparis1);
        garson.takeOrder(orderManager, order1);

        // 4. Şef siparişi tamamlıyor
        sef.completeOrder(orderManager, 1);

        // 5. Garson teslim ediyor
        garson.deliverOrder(orderManager, 1);

        // 6. Garson iptal edilen siparişi oluşturuyor
        ArrayList<MenuItem> siparis2 = new ArrayList<>(Arrays.asList(doner));
        Order order2 = new Order(2, "Masa 3 - Et Döner", siparis2);
        garson.takeOrder(orderManager, order2);
        System.out.println("\n📦 cancel once ram stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n📦 cancel once verıtabanı stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }
        garson.cancelOrder(orderManager, 2);
        System.out.println("\n📦 cancel sonrası ram stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n📦 cancel sonrası verıtabanı stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }
        // 7. Yönetici tüm siparişleri görüntülüyor
        System.out.println("\n📜 Sipariş Geçmişi:");
        mudur.viewAllOrders(orderManager);

        // 8. RAM ve DB stok durumları karşılaştırması
        System.out.println("\n📦 RAM Stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n📦 Veritabanı Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }
    }
}