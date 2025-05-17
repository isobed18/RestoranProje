package org.restoranproje;

import org.restoranproje.db.DatabaseManager;
import org.restoranproje.db.StockDAO;
import org.restoranproje.db.MenuDAO;
import org.restoranproje.model.*;
import org.restoranproje.service.StockManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.setupDatabase();

        StockManager stockManager = new StockManager();
        StockDAO stockDAO = new StockDAO();
        MenuDAO menuDAO = new MenuDAO();

        Waiter garson = new Waiter("Zeynep", "1234");
        Chef sef = new Chef("Ahmet", "abcd");
        Manager mudur = new Manager("Emir", "admin");

        // Add stock items and get their IDs
        StockItem et = new StockItem("Et", 1000.0, "gram", 0.25);
        StockItem domates = new StockItem("Domates", 50.0, "adet", 2.0);
        StockItem sogan = new StockItem("Soğan", 30.0, "adet", 1.5);

        mudur.addStockItem(stockManager, et);
        mudur.addStockItem(stockManager, domates);
        mudur.addStockItem(stockManager, sogan);

        // Get the actual stock items with IDs from the database
        et = stockDAO.getStockItemByName("Et");
        domates = stockDAO.getStockItemByName("Domates");
        sogan = stockDAO.getStockItemByName("Soğan");

        System.out.println("\n✅ RAM'deki Stok Durumu:");
        stockManager.printAllStock();

        System.out.println("\n✅ Veritabanındaki Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " " + item.getUnit());
        }

        // Create menu items with proper stock item references
        ArrayList<StockItem> adanaIngredients = new ArrayList<>(Arrays.asList(
            new StockItem(et.getId(), "Et", 200.0, "gram", 0.25),
            new StockItem(domates.getId(), "Domates", 1.0, "adet", 2.0)
        ));

        ArrayList<StockItem> donerIngredients = new ArrayList<>(Arrays.asList(
            new StockItem(et.getId(), "Et", 150.0, "gram", 0.25),
            new StockItem(sogan.getId(), "Soğan", 1.0, "adet", 1.5)
        ));

        MenuItem adana = new MenuItem("Adana Kebap", "Acılı kebap", MenuItemType.DISH, 80.0, adanaIngredients);
        MenuItem doner = new MenuItem("Et Döner", "Klasik döner", MenuItemType.DISH, 70.0, donerIngredients);

        // Get stock item IDs for menu items
        List<Integer> adanaStockIds = new ArrayList<>();
        for (StockItem item : adanaIngredients) {
            adanaStockIds.add(item.getId());
        }

        List<Integer> donerStockIds = new ArrayList<>();
        for (StockItem item : donerIngredients) {
            donerStockIds.add(item.getId());
        }

        mudur.addMenuItem(menuDAO, adana, adanaStockIds);
        mudur.addMenuItem(menuDAO, doner, donerStockIds);

        // Only print menu and stock status, no order operations
        System.out.println("\n🆕 Veritabanı Stok Durumu:");
        for (StockItem item : stockDAO.getAllStockItems()) {
            System.out.println("[DB] " + item.getName() + " / amount: " + item.getAmount() + " / unit: " + item.getUnit() + " / unitCost: " + item.getUnitCost());
        }

        System.out.println("\n🆕 Veritabanı Menü Durumu:");
        for (MenuItem item : menuDAO.getAllMenuItems()) {
            System.out.println("[DB MENU] " + item.getName() + " - ₺" + item.getPrice());
        }
    }

    public static int insertNewOrder(String details, OrderStatus status) {
        String sql = "INSERT INTO order_history (details, status) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, details);
            pstmt.setString(2, status.toString());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // This is the generated order_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}