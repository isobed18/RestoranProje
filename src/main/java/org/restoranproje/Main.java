package org.restoranproje;

import org.restoranproje.db.DatabaseManager;
import org.restoranproje.db.StockDAO;
import org.restoranproje.db.MenuDAO;
import org.restoranproje.db.OrderDAO;
import org.restoranproje.model.*;
import org.restoranproje.service.OrderManager;
import org.restoranproje.service.StockManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.setupDatabase();

        OrderManager orderManager = new OrderManager();
        StockManager stockManager = orderManager.getStockManager();
        StockDAO stockDAO = new StockDAO();
        MenuDAO menuDAO = new MenuDAO();

        Waiter garson = new Waiter("Zeynep", "1234");
        Chef sef = new Chef("Ahmet", "abcd");
        Manager mudur = new Manager("Emir", "admin");

        mudur.addStockItem(stockManager, new StockItem("Et", 1000.0, "gram", 0.25));
        mudur.addStockItem(stockManager, new StockItem("Domates", 50.0, "adet", 2.0));
        mudur.addStockItem(stockManager, new StockItem("Soğan", 30.0, "adet", 1.5));

        System.out.println("\n✅ RAM'deki Stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n✅ Veritabanındaki Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }

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

        ArrayList<MenuItem> siparis1 = new ArrayList<>(Arrays.asList(adana));
        Order order1 = new Order(1, "Masa 5 - Adana Kebap", siparis1);
        garson.takeOrder(orderManager, order1);

        sef.completeOrder(orderManager, 1);
        garson.deliverOrder(orderManager, 1);

        ArrayList<MenuItem> siparis2 = new ArrayList<>(Arrays.asList(doner));
        Order order2 = new Order(2, "Masa 3 - Et Döner", siparis2);
        garson.takeOrder(orderManager, order2);

        System.out.println("\n📦 cancel once RAM Stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n📦 cancel once Veritabanı Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }

        garson.cancelOrder(orderManager, 2);

        System.out.println("\n📦 cancel sonrası RAM Stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n📦 cancel sonrası Veritabanı Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }

        System.out.println("\n📜 Sipariş Geçmişi:");
        for (Order o : orderManager.getAllOrders()) {
            mudur.update(o);
            System.out.println("🔍 Sipariş içeriği:");
            for (MenuItem mi : o.getItems()) {
                System.out.println("  - " + mi.getName() + " (₺" + mi.getPrice() + "):");
                for (StockItem si : mi.getItems()) {
                    System.out.println("    * " + si.getAmount() + " " + si.getUnit() + " " + si.getName());
                }
            }
        }

        System.out.println("\n📂 Veritabanından Aktif Siparişler:");
        List<Order> activeOrders = OrderDAO.getActiveOrders();
        for (Order dbOrder : activeOrders) {
            System.out.println(dbOrder);
            System.out.println("🔍 Sipariş içeriği:");
            for (MenuItem mi : dbOrder.getItems()) {
                System.out.println("  - " + mi.getName() + " (₺" + mi.getPrice() + "):");
                for (StockItem si : mi.getItems()) {
                    System.out.println("    * " + si.getAmount() + " " + si.getUnit() + " " + si.getName());
                }
            }
        }

        System.out.println("\n📦 RAM Stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n📦 Veritabanı Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }

        System.out.println("\n🧪 YÖNETİCİ TESTİ: Menü ve Stok Ürünlerini Kaldır/Güncelle");
        mudur.removeStockItem(stockDAO, "Domates");
        mudur.removeMenuItem(menuDAO, "Adana Kebap");
        mudur.changeUnitCost(stockDAO, "Soğan", 3.0);

        System.out.println("\n🆕 Veritabanı Stok Durumu (Silme ve Güncelleme Sonrası):");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " / unit: " + item.getUnit() + " / unitCost: " + item.getUnitCost());
        }

        System.out.println("\n🆕 Veritabanı Menü Durumu (Silme Sonrası):");
        for (MenuItem item : menuDAO.getAllMenuItems()) {
            System.out.println("[DB MENU] " + item.getName() + " - ₺" + item.getPrice());
        }
    }
}